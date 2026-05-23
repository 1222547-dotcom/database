package phase3;

import phase3.DBConfig;
import phase3.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 🛠️ FIX: Use centralized fallback connection logic
            con = DBConfig.getConnection();
            if (con == null) return products;

            System.out.println("Connection established");

            String SQL = "SELECT p.*, s.Supplier_Name FROM Product p " +
                    "LEFT JOIN Supplier s ON p.Supplier_ID = s.Supplier_ID " +
                    "ORDER BY p.Product_ID";

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("Product_ID"));
                p.setProductName(rs.getString("Product_Name"));
                p.setBrand(rs.getString("Brand"));
                p.setModel(rs.getString("Model"));
                p.setColor(rs.getString("Color"));
                p.setRam(rs.getString("RAM"));
                p.setStorage(rs.getString("Storage"));
                p.setLastPurchasePrice(rs.getDouble("Last_Purchase_Price"));
                p.setSellingPrice(rs.getDouble("Selling_Price"));
                p.setSupplierId(rs.getInt("Supplier_ID"));
                p.setSupplierName(rs.getString("Supplier_Name") != null ? rs.getString("Supplier_Name") : "");
                products.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 🛡️ Ensure resources are closed cleanly even if queries fail
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) {
                    con.close();
                    System.out.println("Connection closed");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return products;
    }

    public boolean addProduct(Product p) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 🛠️ FIX: Use centralized fallback connection logic
            con = DBConfig.getConnection();
            if (con == null) return false;

            String SQL = "INSERT INTO Product (Product_Name, Brand, Model, Color, RAM, Storage, " +
                    "Last_Purchase_Price, Selling_Price, Supplier_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, p.getProductName());
            pstmt.setString(2, p.getBrand());
            pstmt.setString(3, p.getModel());
            pstmt.setString(4, p.getColor());
            pstmt.setString(5, p.getRam());
            pstmt.setString(6, p.getStorage());
            pstmt.setDouble(7, p.getLastPurchasePrice());
            pstmt.setDouble(8, p.getSellingPrice());
            pstmt.setInt(9, p.getSupplierId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean updateProduct(Product p) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConfig.getConnection();
            if (con == null) return false;

            String SQL = "UPDATE Product SET Product_Name=?, Brand=?, Model=?, Color=?, RAM=?, " +
                    "Storage=?, Last_Purchase_Price=?, Selling_Price=?, Supplier_ID=? WHERE Product_ID=?";

            pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, p.getProductName());
            pstmt.setString(2, p.getBrand());
            pstmt.setString(3, p.getModel());
            pstmt.setString(4, p.getColor());
            pstmt.setString(5, p.getRam());
            pstmt.setString(6, p.getStorage());
            pstmt.setDouble(7, p.getLastPurchasePrice());
            pstmt.setDouble(8, p.getSellingPrice());
            pstmt.setInt(9, p.getSupplierId());
            pstmt.setInt(10, p.getProductId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean deleteProduct(int productId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConfig.getConnection();
            if (con == null) return false;

            String SQL = "DELETE FROM Product WHERE Product_ID = ?";

            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, productId);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConfig.getConnection();
            if (con == null) return products;

            String SQL = "SELECT p.*, s.Supplier_Name FROM Product p " +
                    "LEFT JOIN Supplier s ON p.Supplier_ID = s.Supplier_ID " +
                    "WHERE p.Product_Name LIKE ? OR p.Brand LIKE ? OR p.Model LIKE ?";

            pstmt = con.prepareStatement(SQL);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("Product_ID"));
                p.setProductName(rs.getString("Product_Name"));
                p.setBrand(rs.getString("Brand"));
                p.setModel(rs.getString("Model"));
                p.setColor(rs.getString("Color"));
                p.setRam(rs.getString("RAM"));
                p.setStorage(rs.getString("Storage"));
                p.setLastPurchasePrice(rs.getDouble("Last_Purchase_Price"));
                p.setSellingPrice(rs.getDouble("Selling_Price"));
                p.setSupplierId(rs.getInt("Supplier_ID"));
                p.setSupplierName(rs.getString("Supplier_Name") != null ? rs.getString("Supplier_Name") : "");
                products.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return products;
    }

    public List<String[]> getSuppliers() {
        List<String[]> suppliers = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DBConfig.getConnection();
            if (con == null) return suppliers;

            String SQL = "SELECT Supplier_ID, Supplier_Name FROM Supplier ORDER BY Supplier_Name";

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                suppliers.add(new String[]{
                        String.valueOf(rs.getInt("Supplier_ID")),
                        rs.getString("Supplier_Name")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error getting suppliers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return suppliers;
    }
}