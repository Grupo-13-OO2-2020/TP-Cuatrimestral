package Grupo13OO2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import Grupo13OO2.Entities.User;
import Grupo13OO2.Models.EmpleadoModel;
import Grupo13OO2.Models.LocalModel;
import Grupo13OO2.Models.LocalesModels;
import Grupo13OO2.Models.ProductoModel;
import Grupo13OO2.helpers.ViewRouteHelper;
import Grupo13OO2.repositories.IUserRepository;
import Grupo13OO2.services.IEmpleadoService;
import Grupo13OO2.services.ILocalService;
import Grupo13OO2.services.ILoteService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/locales")
public class LocalController {

	@Autowired
	@Qualifier("localService")
	private ILocalService localService;

	@Autowired
	@Qualifier("loteService")
	private ILoteService loteService;

	@Autowired
	@Qualifier("empleadoService")
	private IEmpleadoService empleadoService;

	@Autowired
	private IUserRepository userRepository;

	@GetMapping("")
	public ModelAndView index(@RequestParam Map<String, Object> params, Model model) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_INDEX);
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

		PageRequest pageRequest = PageRequest.of(page, 5);

		Page<LocalModel> pageLocal = localService.getAllPages(pageRequest);

		int totalPage = pageLocal.getTotalPages();
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			mAV.addObject("pages", pages);

		}
		mAV.addObject("locales", pageLocal.getContent());
		mAV.addObject("current", page + 1);
		mAV.addObject("next", page + 2);
		mAV.addObject("prev", page);
		mAV.addObject("last", totalPage);

		// agrego datos de ususario
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("local", localService.findById(e.getLocal().getId()));
		mAV.addObject("empleado", e);

		return mAV;
	}

	@GetMapping("/main/{id}")
	public ModelAndView main(@PathVariable("id") int id) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_MAIN);
		mAV.addObject("local", localService.findById(id));
		mAV.addObject("locales", localService.getAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);

		return mAV;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/new")
	public ModelAndView create() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_FORM);
		mAV.addObject("local", new LocalModel());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		return mAV;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/save")
	public String create(@Valid @ModelAttribute("local") LocalModel localModel, BindingResult result) {
		if (result.hasErrors()) {

			return ViewRouteHelper.LOCAL_FORM;

		}
		localService.insertOrUpdate(localModel);

		return "redirect:/locales/";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editar/{id}")
	public ModelAndView get(@PathVariable("id") int id) {

		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_FORM);
		mAV.addObject("local", localService.findById(id));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		return mAV;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/eliminar/{id}")
	public RedirectView delete(Model model, @PathVariable("id") int id,RedirectAttributes redirect) {
		if(localService.findById(id).getEmpleados().isEmpty()) {
			localService.delete(id);
			return new RedirectView("/locales");

		}else {
			redirect.addFlashAttribute("popUp", "error");
		return new RedirectView("/locales");
			
		}		
	
	}

	@GetMapping("/calculacoordenadas")
	public ModelAndView calculacoordenadas() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_CALC_COORD);
		mAV.addObject("locales", localService.getAll());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		mAV.addObject("local", localService.findById(e.getLocal().getId()));

		return mAV;
	}

	public double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
		double radioTierra = 6371; // en kilómetros
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double va1 = Math.pow(sindLat, 2)
				+ Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
		return radioTierra * va2;
	}

	@RequestMapping(value = "/obtenerdistancia", method = RequestMethod.POST)
	public ModelAndView sacardistancia(LocalesModels locales, Model model) {
		model.addAttribute("lat1", localService.findById(locales.getPrimerLocal().getId()).getLatitud());
		model.addAttribute("lng1", localService.findById(locales.getPrimerLocal().getId()).getLongitud());
		model.addAttribute("dir1", localService.findById(locales.getPrimerLocal().getId()).getDireccion());

		model.addAttribute("lat2", localService.findById(locales.getSegundoLocal().getId()).getLatitud());
		model.addAttribute("lng2", localService.findById(locales.getSegundoLocal().getId()).getLongitud());
		model.addAttribute("dir2", localService.findById(locales.getSegundoLocal().getId()).getDireccion());

		ModelAndView mAV = new ModelAndView("/local/damedistancia");
		double distancia = distanciaCoord(localService.findById(locales.getPrimerLocal().getId()).getLatitud(),
				localService.findById(locales.getPrimerLocal().getId()).getLongitud(),
				localService.findById(locales.getSegundoLocal().getId()).getLatitud(),
				localService.findById(locales.getSegundoLocal().getId()).getLongitud());
		model.addAttribute("distancia", Math.round(distancia * 100) / 100.00);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);
		mAV.addObject("local", localService.findById(e.getLocal().getId()));
		return mAV;
	}

	@RequestMapping(value = "/reporte/{id}", method = RequestMethod.POST)
	public ModelAndView sacarprodfechas(@PathVariable("id") int id,
			@RequestParam("fecha1") @DateTimeFormat(pattern = "yy-MM-dd") Date fecha1,
			@RequestParam("fecha2") @DateTimeFormat(pattern = "yy-MM-dd") Date fecha2, Model model) {

		ModelAndView mAV = new ModelAndView("producto/reporte-local");
		LocalModel local = localService.findById(id);
		Set<ProductoModel> listProduc = localService.productosVendidosEntreFechas(local, fecha1, fecha2);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		mAV.addObject("fecha1", formatter.format(fecha1));
		mAV.addObject("fecha2", formatter.format(fecha2));
		mAV.addObject("local", local);
		mAV.addObject("productosFecha", listProduc);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("empleado", e);

		return mAV;
	}
	
	@GetMapping("/listado")
	public ModelAndView listado(@RequestParam Map<String, Object> params, Model model) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.LOCAL_LISTADO);
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;

		PageRequest pageRequest = PageRequest.of(page, 5);

		Page<LocalModel> pageLocal = localService.getAllPages(pageRequest);

		int totalPage = pageLocal.getTotalPages();
		if (totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			mAV.addObject("pages", pages);

		}
		mAV.addObject("locales", pageLocal.getContent());
		mAV.addObject("current", page + 1);
		mAV.addObject("next", page + 2);
		mAV.addObject("prev", page);
		mAV.addObject("last", totalPage);

		// agrego datos de ususario
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		mAV.addObject("usuario", auth.getName());
		User u = userRepository.findByUsernameAndFetchUserRolesEagerly(auth.getName());
		EmpleadoModel e = empleadoService.ListarId(u.getEmpleado().getId());
		mAV.addObject("local", localService.findById(e.getLocal().getId()));
		mAV.addObject("empleado", e);

		return mAV;
	}
}
