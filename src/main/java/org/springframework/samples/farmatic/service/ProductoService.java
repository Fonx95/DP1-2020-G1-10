package org.springframework.samples.farmatic.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
	
	private ProductoRepository productoRepository;
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Collection<Producto> findProducts() throws DataAccessException{
		//lista productos
		return productoRepository.findALL();
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Producto findProductoById(int id) throws DataAccessException {
		//detalles productos
		return productoRepository.findById(id);
	}
	
	@Autowired
	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}
	
	@Transactional
	public void saveProducto(Producto product) throws DataAccessException {
		//creating product
		productoRepository.save(product);		
	}	

}
