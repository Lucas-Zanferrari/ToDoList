package br.senai.sp.informatica.todolist.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.*;

@Entity
public class Lista {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String titulo;

	@OneToMany(mappedBy="lista", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemLista> itens;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<ItemLista> getItens() {
		return itens;
	}
	public void setItens(List<ItemLista> itens) {
		this.itens = itens;
	}

	@JsonProperty(value = "feito")
	public boolean isRealizada() {
		for(ItemLista item : itens) {
			if(!item.isFeito()) {
				return false;
			}
		}
		return true;
	}	
}
