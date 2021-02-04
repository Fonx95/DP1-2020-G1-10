<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="farmatic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<farmatic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</farmatic:menuItem>
				
				<sec:authorize access= "hasAuthority('farmaceutico') || hasAuthority('admin')">
					<farmatic:menuItem active="${name eq 'ventas'}" url="/ventas/actual"
						title="ventas" dropdown="${true}">
						<ul class="dropdown-menu">
							<li class="btn-menu">								
									<div class="row">
										<div class="text-center">																					
												<a href="<c:url value="/ventas/actual" />" class="btn btn-a">Venta Actual</a>
										</div>																					
									</div>						
							</li>
							<li class="divider"></li>
							<li class="btn-menu">								
									<div class="row">
										<div class="text-center">																					
												<a href="<c:url value="/ventas" />" class="btn btn-a">Historial de Ventas</a>
										</div>																					
									</div>						
							</li>														
						</ul>
					</farmatic:menuItem>
				</sec:authorize>
				
				<sec:authorize access= "hasAuthority('farmaceutico') || hasAuthority('admin')">
					<farmatic:menuItem active="${name eq 'pedidos'}" url="/pedidos/actual"
						title="pedidos" dropdown="${true}">
						<ul class="dropdown-menu">
							<li class="btn-menu">								
									<div class="row">
										<div class="text-center">																					
												<a href="<c:url value="/pedidos/actual" />" class="btn btn-a">Pedido Actual</a>
										</div>																					
									</div>						
							</li>
							<li class="divider"></li>
							<li class="btn-menu">								
									<div class="row">
										<div class="text-center">																					
												<a href="<c:url value="/pedidos" />" class="btn btn-a">Historial de Pedidos</a>
										</div>																					
									</div>						
							</li>														
						</ul>
					</farmatic:menuItem>
				</sec:authorize>				

				<sec:authorize access= "hasAuthority('proveedor')">
					<farmatic:menuItem active="${name eq 'pedidos'}" url="/proveedor"
						title="pedidos">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Pedidos</span>
					</farmatic:menuItem>
				</sec:authorize>
				<sec:authorize access= "hasAuthority('cliente')">
					<farmatic:menuItem active="${name eq 'ventasCliente'}" url="/cliente/ventas"
						title="ventas">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Mis Ventas</span>
					</farmatic:menuItem>
				</sec:authorize>
				<sec:authorize access= "hasAuthority('farmaceutico') || hasAuthority('admin')">
					<farmatic:menuItem active="${name eq 'productos'}" url="/productos"
						title="productos">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Productos</span>
					</farmatic:menuItem>
				</sec:authorize>
				
				<sec:authorize access= "hasAuthority('farmaceutico') || hasAuthority('admin')">
					<farmatic:menuItem active="${name eq 'clientes'}" url="/clientes"
						title="clientes">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
						<span>Clientes</span>
					</farmatic:menuItem>
				</sec:authorize>

				<farmatic:menuItem active="${name eq 'error'}" url="/oups"
					title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
					<span>Error</span>
				</farmatic:menuItem>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					
					<farmatic:menuItem active="${name eq 'login'}" url="/login" title="login">
						<span>Login</span>
					</farmatic:menuItem>
					<farmatic:menuItem active="${name eq 'registro'}" url="/users/new" title="registro">
						<span>Register</span>
					</farmatic:menuItem>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<farmatic:menuItem active="${name eq 'usuario'}" url="" title="Usuario" dropdown="${true}">
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>			
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<sec:authorize access= "hasAuthority('cliente') || hasAuthority('proveedor') || hasAuthority('farmaceutico')">
													<a href="/users" class="btn btn-primary btn-block">Mi perfil</a>
												</sec:authorize>
												<a href="/users/password" class="btn btn-danger btn-block">Change Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
						</ul>
					</farmatic:menuItem>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
