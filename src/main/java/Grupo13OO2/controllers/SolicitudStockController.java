package Grupo13OO2.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import Grupo13OO2.Entities.User;
import Grupo13OO2.Models.EmpleadoModel;
import Grupo13OO2.Models.LocalModel;
import Grupo13OO2.Models.SolicitudStockModel;
import Grupo13OO2.converters.SolicitudStockConverter;
import Grupo13OO2.helpers.ViewRouteHelper;
import Grupo13OO2.repositories.IUserRepository;
import Grupo13OO2.services.IClienteService;
import Grupo13OO2.services.IEmpleadoService;
import Grupo13OO2.services.ILocalService;
import Grupo13OO2.services.IProductoService;
import Grupo13OO2.services.ISolicitudStockService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/solicitudesStock")
public class SolicitudStockController {
	@Autowired
	@Qualifier("solicitudStockConverter")
	private SolicitudStockConverter solicitudStockConverter;

	@Autowired
	@Qualifier("solicitudStockService")
	private ISolicitudStockService solicitudStockService;

	@Autowired
	@Qualifier("productoService")
	private IProductoService productoService;

	@Autowired
	@Qualifier("empleadoService")
	private IEmpleadoService empleadoService;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	@Qualifier("clienteService")
	private IClienteService clienteService;

	@Autowired
	@Qualifier("localService")
	private ILocalService localService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("")
	public ModelAndView index(@RequestParam Map<String, Object> params, Model model) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.SOLICITUDSTOCK_INDEX);
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 5);

		Page<SolicitudStockModel> pageSolicitud = solicitudStockService.getAllPages(pageRequest);

		int totalPage = pageSolicitud.getTotalPages();
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			mAV.addObject("pages", pages);
		}
		mAV.addObject("solicitudesStock", pageSolicitud.getContent());
		mAV.addObject("current", page + 1);
		mAV.addObject("next", page + 2);
		mAV.addObject("prev", page);
		mAV.addObject("last", totalPage);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);

		return mAV;
	}

	@GetMapping("{id}")
	public ModelAndView local(@PathVariable("id") int id, @RequestParam Map<String, Object> params, Model model) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.SOLICITUDSTOCK_INDEX_LOCAL);
		mAV.addObject("local", localService.findById(id));
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 5);

		Page<SolicitudStockModel> pageSolicitud = solicitudStockService.getAllPagesLocal(pageRequest, id);

		int totalPage = pageSolicitud.getTotalPages();
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			mAV.addObject("pages", pages);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			mAV.addObject("usuario", auth.getName());
		}
		mAV.addObject("solicitudesStock", pageSolicitud.getContent());
		mAV.addObject("current", page + 1);
		mAV.addObject("next", page + 2);
		mAV.addObject("prev", page);
		mAV.addObject("last", totalPage);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);

		return mAV;
	}

	@GetMapping("/new/{id}")
	public ModelAndView create(@PathVariable("id") int id) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.SOLICITUDSTOCK_FORM);
		mAV.addObject("local", localService.findById(id));
		mAV.addObject("solicitudStock", new SolicitudStockModel());
		mAV.addObject("productos", productoService.getAll());
		mAV.addObject("clientes", clienteService.getAll());
		mAV.addObject("locales", localService.getAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		return mAV;
	}

	@PostMapping("/save")
	public RedirectView create(@ModelAttribute("solicitudStock") SolicitudStockModel solicitudStockModel,
			RedirectAttributes redirect) {
		// datos de usuario
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		solicitudStockModel.setProducto(productoService.ListarId(solicitudStockModel.getProducto().getId()));
		solicitudStockService.insertOrUpdate(solicitudStockModel);
		if (solicitudStockModel.isAceptado()) {
			if (localService.validarStockLocal(solicitudStockModel.getProducto().getCodigoProducto(),
					solicitudStockModel.getCantidad(), e.getLocal().getId())==true) {
				localService.consumirLoteSolicitud(solicitudStockModel);
				return new RedirectView("/solicitudesStock/" + e.getLocal().getId());
			} else {
				solicitudStockModel.setAceptado(false);
				redirect.addFlashAttribute("sinStock", "sinStock");
				return new RedirectView("/solicitudesStock/" + e.getLocal().getId());
			}

		}
		if(solicitudStockModel.getId()>0) {
		redirect.addFlashAttribute("sinAceptar", "sinAceptar");}

		return new RedirectView("/solicitudesStock/" + e.getLocal().getId());
	}

	@GetMapping("/editar/{id}")
	public ModelAndView get(@PathVariable("id") int id) {

		ModelAndView mAV = new ModelAndView(ViewRouteHelper.SOLICITUDSTOCK_FORM_LOCAL);
		SolicitudStockModel solicitudStockModel = solicitudStockService.ListarId(id);
		mAV.addObject("solicitudStock", solicitudStockModel);
		mAV.addObject("productos", productoService.getAll());
		mAV.addObject("clientes", clienteService.getAll());
		mAV.addObject("locales", localService.getAll());
		mAV.addObject("local", localService.findById(solicitudStockModel.getVendedor().getLocal().getId()));
		// datos de usuario
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		return mAV;
	}

	@GetMapping("/eliminar/{id}")
	public RedirectView delete(Model model, @PathVariable int id) {
		solicitudStockService.delete(id);
		return new RedirectView("/solicitudesStock");
	}

	@GetMapping("/localesDestinatarios")
	public @ResponseBody List<LocalModel> localesDestinatarios(int idProducto, int idVendedor, int cantidad) {
		List<LocalModel> locales = solicitudStockService.getLocalesCercanos(idProducto, idVendedor, cantidad);
		return locales;
	}

}
