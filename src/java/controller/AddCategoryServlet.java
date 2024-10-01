package controller;

import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;

public class AddCategoryServlet extends HttpServlet {

    // Định nghĩa các hằng số
    private static final String ERROR_MESSAGE = "error";
    private static final String MANAGER_CATEGORY = "managercategory";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddcategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddcategoryServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cname = request.getParameter("categoryname");
        String categoryidString = request.getParameter("categoryid");

        // Kiểm tra xem categoryid hoặc categoryname có trống hoặc chỉ chứa khoảng trắng không
        if (categoryidString == null || categoryidString.trim().isEmpty() || cname == null || cname.trim().isEmpty()) {
            request.setAttribute(ERROR_MESSAGE, "Category ID or Name cannot be empty.");
            request.getRequestDispatcher(MANAGER_CATEGORY).forward(request, response);
            return;
        }

        // Kiểm tra xem categoryid có phải là số nguyên không
        int cid;
        try {
            cid = Integer.parseInt(categoryidString);
        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_MESSAGE, "Category ID must be an integer.");
            request.getRequestDispatcher(MANAGER_CATEGORY).forward(request, response);
            return;
        }

        // Kiểm tra xem category có tồn tại không
        ProductDAO productData = new ProductDAO();
        Category categoryExists = productData.getCategoryByID(cid);

        if (categoryExists != null) {
            request.setAttribute(ERROR_MESSAGE, "Category with this ID already exists.");
            request.getRequestDispatcher(MANAGER_CATEGORY).forward(request, response);
        } else {
            // Nếu category không tồn tại, thêm mới category
            productData.AddCategory(cid, cname);
            response.sendRedirect(MANAGER_CATEGORY);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
