package br.com.treinaweb.twgerenciadortarefas.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.treinaweb.twgerenciadortarefas.modelos.Tarefa;
import br.com.treinaweb.twgerenciadortarefas.repositorios.RepositorioTarefas;

@Controller
@RequestMapping("/tarefas")
public class TarefasController {

	@Autowired
	private RepositorioTarefas repositorioTarefa;

	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/listar.html");
		mv.addObject("tarefa", repositorioTarefa.findAll());
		return mv;
	}

	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/inserir");

		mv.addObject("tarefa", new Tarefa());
		return mv;
	}

	@PostMapping("/inserir")
	public ModelAndView inserir(@Valid Tarefa tarefa, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		if (tarefa.getDataExp() == null) {
			result.rejectValue("dataExp", "tarefa.dataExpInv", "A Data de Expiração não pode estar vazia");
		} else {
			if (tarefa.getDataExp().before(new Date())) {
				result.rejectValue("dataExp", "tarefa.dataExpInv",
						"A Data de Expiração não pode ser anterior a data atual");
			}
		}
		if (result.hasErrors()) {
			mv.setViewName("tarefas/inserir");
			mv.addObject(tarefa);
		} else {
			mv.setViewName("redirect:/tarefas/listar");
			repositorioTarefa.save(tarefa);
		}
		return mv;
	}

	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView();
		Tarefa tarefa = repositorioTarefa.getOne(id);
		mv.addObject("tarefa", tarefa);
		mv.setViewName("tarefas/alterar");
		return mv;

	}

	@PostMapping("/alterar")
	public ModelAndView alterar(@Valid Tarefa tarefa, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		if (tarefa.getDataExp() == null) {
			result.rejectValue("dataExp", "tarefa.dataExpInv", "A Data de Expiração não pode estar vazia");
		} else {
			if (tarefa.getDataExp().before(new Date())) {
				result.rejectValue("dataExp", "tarefa.dataExpInv",
						"A Data de Expiração não pode ser anterior a data atual");
			}
		}
		if (result.hasErrors()) {
			mv.setViewName("tarefas/alterar");
			mv.addObject(tarefa);
		} else {
			mv.setViewName("redirect:/tarefas/listar");
			repositorioTarefa.save(tarefa);
		}
		return mv;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id) {
		repositorioTarefa.deleteById(id);
		return "redirect:/tarefas/listar";
	}

	@GetMapping("/concluir/{id}")
	public String concluir(@PathVariable("id") Long id) {
		Tarefa tarefa = repositorioTarefa.getOne(id);
		tarefa.setConcluida(true);
		repositorioTarefa.save(tarefa);
		return "redirect:/tarefas/listar";
	}
}
