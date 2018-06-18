package pe.edu.bpz.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.bpz.model.entity.Factura;
import pe.edu.bpz.model.entity.Proveedor;
import pe.edu.bpz.service.IFacturaService;
import pe.edu.bpz.service.IProveedorService;
import pe.edu.bpz.viewmodel.FacturaViewModel;
@Controller
@SessionAttributes("factura")
@RequestMapping(value="/factura")
public class FacturaController {
	@Autowired
	private IProveedorService pService;
	
	@Autowired
	private IFacturaService fService;
	
	
	
	@GetMapping(value = "/crear/{proveedorId}")
	public String crear(Model model, @PathVariable(value="proveedorId") Long id,RedirectAttributes flash) {
		
		Proveedor proveedor=pService.findbyId(id);
		
		if(proveedor==null) {
			flash.addFlashAttribute("error", "The client does not exist in the database");
			return "redirect:/factura/listar";
		}
		
		Factura factura = new Factura();
		factura.setProveedor(proveedor);
		
		/*Patrón viewmodel*/
		model.addAttribute("factura", factura);
		
		model.addAttribute("titulo", "Crear Factura");
		
		return "factura/crear";
	}
	
	
	@PostMapping(value="/crear")
	public String guardar(@Valid Factura factura, BindingResult result,Model model, RedirectAttributes flash,
			SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/crear";
		}
		
		fService.save(factura);
		
		status.setComplete();
		flash.addFlashAttribute("success", "Invoice created successfully!");
		
		return "redirect:/proveedor/detalle/" + factura.getProveedor().getIdProveedor();
	}
	
	
//	@RequestMapping(value = "/crear/{id}")
//	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
//
//		Factura factura = null;
//
//		if (id > 0) {
//			factura = fService.findbyId(id);
//			if (factura == null) {
//				flash.addFlashAttribute("error", "El ID de la factura no existe en la BBDD!");
//				return "redirect:/factura/listar";
//			}
//		} else {
//			flash.addFlashAttribute("error", "El ID de la factura no puede ser cero!");
//			return "redirect:/factura/listar";
//		}
//		model.addAttribute("factura", factura);
//		model.addAttribute("titulo", "Editar Factura");
//		return "crearFactura";
//	}
	
	@InitBinder     
	public void initBinder(WebDataBinder binder){
	     binder.registerCustomEditor(       java.sql.Date.class,     
	                         new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));   
	}

}
