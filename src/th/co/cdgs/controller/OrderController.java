package th.co.cdgs.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import th.co.cdgs.bean.CustomerDto;
import th.co.cdgs.bean.OrderDto;
import th.co.cdgs.bean.ProductDto;


@Path("order")
public class OrderController {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryOrderById (@PathParam("id") Long orderId ) {
		ResultSet rs = null;
		PreparedStatement pst = null; //query
		Connection conn = null;  //DB
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement("SELECT order_Id,c.customer_id" + "CONCAT(first_name,' ',last_name) as full_name,"
					+ "p.product_id,product_name,price,amount,"
					+ "order_date,order_status from orders o ,"
					+ "left join product p on p.product_id = o.product_id ,"
					+ "left join customer c on c.customer_Id = o.customer_Id  WHERE order_id = ? ");
			rs = pst.executeQuery();
			while (rs.next()) {
				OrderDto order = new OrderDto();
				CustomerDto customer = new CustomerDto();
				ProductDto product = new ProductDto();
				order.setOrderId(rs.getLong("order_Id"));
				customer.setCustomerId(rs.getLong("customer_Id"));
				customer.setFullname(rs.getString("full_name"));
			    customer.setTel(rs.getString("tel"));
			    customer.setEmail(rs.getString("email"));
			    order.setCustomer(customer);
				product.setProductId(rs.getLong("product_Id"));
				product.setProductName(rs.getString("product_name"));
				product.setProductDesc(rs.getString("product_desc"));
				order.setProduct(product);
				order.setAmount(rs.getLong("amount"));
				order.setTotal(rs.getBigDecimal("price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setStatus(rs.getString("order_status"));
				//list.add(order);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.status(Status.OK).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrder(OrderDto order) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement(
					"INSERT INTO orders (customer_id,product_id,amount,order_date,order_status)" + "VALUES (?,?,?,?,?)");
			int index = 1;
			pst.setLong(index++, order.getCustomer().getCustomerId());
			pst.setLong(index++, order.getProduct().getProductId());
			pst.setLong(index++, order.getAmount());
			pst.setDate(index++, new Date(order.getOrderDate().getTime()));
			pst.setString(index++, order.getStatus());
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {

				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.CREATED).entity(order).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrder (OrderDto order) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement("UPDATE order  SET " + "customer_id = ? ," + "product_id = ?,"
					+ "amount = ? , " + "order_date = ? ," + "order_status = ?" + "WHERE order_Id = ?");
			int index = 1;
			pst.setLong(index++, order.getCustomer().getCustomerId());
			pst.setLong(index++, order.getProduct().getProductId());
			pst.setLong(index++, order.getAmount());
			pst.setDate(index++, new Date(order.getOrderDate().getTime()));
			pst.setString(index++, order.getStatus());
			pst.setLong(index++, order.getOrderId());
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.CREATED).entity(order).build();
	}
	
	@GET
	@Path("queryOrderByCondition")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryOrderByCondition(@QueryParam("firstName") String firstName,@QueryParam("lastName") String lastName,
										  @QueryParam("email") String email,@QueryParam("productId")Long productId )  {
		List<OrderDto> list = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement pst = null; //query
		Connection conn = null;  //DB
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement("SELECT order_Id,c.customer_id" + "CONCAT(first_name,' ',last_name) as full_name,"
					+ "email,tel,"
					+ "o.product_id,product_name,product_desc,amount,price from orders o,"
					+ "left join product p on p.product_id = o.product_id ,"
					+ "left join customer c on c.customer_Id = o.customer_Id "
					+ "WHERE 1=1 ");
			rs = pst.executeQuery();
			OrderDto orderDto = null;
			if (firstName != null && !"".equals(firstName)) {
				
			}
			if (lastName != null && !"".equals(lastName)) {
				
			}
			if (email != null && !"".equals(email)) {
				
			}
			if (productId  != null ) {
				
			}
			while (rs.next()) {
				OrderDto order = new OrderDto();
				CustomerDto customer = new CustomerDto();
				ProductDto product = new ProductDto();
				order.setOrderId(rs.getLong("order_Id"));
			    customer.setCustomerId(rs.getLong("customer_Id"));
			    customer.setFullname(rs.getString("full_name"));
			    customer.setTel(rs.getString("tel"));
			    customer.setEmail(rs.getString("email"));
			    order.setCustomer(customer);
				product.setProductId(rs.getLong("product_Id"));
				product.setProductName(rs.getString("product_name"));
				product.setProductDesc(rs.getString("product_desc"));
				order.setProduct(product);
				order.setAmount(rs.getLong("amount"));
				order.setTotal(rs.getBigDecimal("price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setStatus(rs.getString("order_status"));
				list.add(orderDto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Response.ok().entity(list).build();
	}
	
	@DELETE
	public Response deleteOrder (Long orderId) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workshop", "root", "p@ssw0rd");
			pst = conn.prepareStatement(
					"DELETE FROM orders  WHERE order_id = ? ");
			int index = 1;
			pst.setLong(index++, orderId);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {

				try {
					rs.close();
				} catch (SQLException e) {

				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return Response.status(Status.OK).build();
	}

}
