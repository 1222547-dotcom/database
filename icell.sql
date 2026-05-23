-- إنشاء قاعدة البيانات
CREATE DATABASE IF NOT EXISTS iCell_DB;
USE iCell_DB;

CREATE TABLE IF NOT EXISTS User (
    User_ID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    First_Name VARCHAR(50),
    Last_Name VARCHAR(50),
    Email VARCHAR(100),
    Phone_Number VARCHAR(20),
    Birth_Date DATE,
    User_Type VARCHAR(20),
    Access_Level INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS Warehouse (
    Warehouse_ID INT PRIMARY KEY AUTO_INCREMENT,
    Warehouse_Name VARCHAR(100) NOT NULL,
    City VARCHAR(50),
    Address VARCHAR(200),
    Capacity INT
);

CREATE TABLE IF NOT EXISTS Supplier (
    Supplier_ID INT PRIMARY KEY AUTO_INCREMENT,
    Supplier_Name VARCHAR(100) NOT NULL,
    Phone_Number VARCHAR(20),
    Email VARCHAR(100),
    Country VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Product (
    Product_ID INT PRIMARY KEY AUTO_INCREMENT,
    Product_Name VARCHAR(100) NOT NULL,
    Brand VARCHAR(50),
    Model VARCHAR(50),
    Color VARCHAR(30),
    RAM VARCHAR(10),
    Storage VARCHAR(20),
    Last_Purchase_Price DECIMAL(10,2),
    Selling_Price DECIMAL(10,2),
    Supplier_ID INT,
    FOREIGN KEY (Supplier_ID) REFERENCES Supplier(Supplier_ID)
);

CREATE TABLE IF NOT EXISTS Inventory (
    Inventory_ID INT PRIMARY KEY AUTO_INCREMENT,
    Product_ID INT,
    Warehouse_ID INT,
    Quantity INT DEFAULT 0,
    Last_Update DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID),
    FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(Warehouse_ID)
);

CREATE TABLE IF NOT EXISTS Shop (
    Shop_ID INT PRIMARY KEY AUTO_INCREMENT,
    Shop_Name VARCHAR(100) NOT NULL,
    Owner_Name VARCHAR(100),
    City VARCHAR(50),
    Street VARCHAR(100),
    Total_Debt DECIMAL(10,2) DEFAULT 0,
    Total_Purchases DECIMAL(10,2) DEFAULT 0,
    Total_Payments DECIMAL(10,2) DEFAULT 0,
    User_ID INT,
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

CREATE TABLE IF NOT EXISTS Employee (
    Employee_ID INT PRIMARY KEY AUTO_INCREMENT,
    User_ID INT,
    Job_Title VARCHAR(50),
    Salary DECIMAL(10,2),
    City VARCHAR(50),
    Street VARCHAR(100),
    Date_Of_Employment DATE,
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

CREATE TABLE IF NOT EXISTS Employee_Warehouse_Assignment (
    Assignment_ID INT PRIMARY KEY AUTO_INCREMENT,
    Employee_ID INT,
    Warehouse_ID INT,
    Assignment_Date DATE,
    FOREIGN KEY (Employee_ID) REFERENCES Employee(Employee_ID),
    FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(Warehouse_ID)
);

CREATE TABLE IF NOT EXISTS Invoice (
    Invoice_ID INT PRIMARY KEY AUTO_INCREMENT,
    Invoice_Date DATE,
    Shop_ID INT,
    Warehouse_ID INT,
    Created_By INT,
    Total_Amount DECIMAL(10,2),
    Approval_Status VARCHAR(20) DEFAULT 'Pending',
    Approved_By INT,
    Approval_Date DATE,
    Notes TEXT,
    VAT DECIMAL(10,2) DEFAULT 0,
    Payment_Status VARCHAR(20) DEFAULT 'Unpaid',
    Shipping_Cost DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (Shop_ID) REFERENCES Shop(Shop_ID),
    FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(Warehouse_ID),
    FOREIGN KEY (Created_By) REFERENCES User(User_ID),
    FOREIGN KEY (Approved_By) REFERENCES User(User_ID)
);

CREATE TABLE IF NOT EXISTS Invoice_Details (
    Invoice_Detail_ID INT PRIMARY KEY AUTO_INCREMENT,
    Invoice_ID INT,
    Product_ID INT,
    Quantity INT,
    Unit_Price DECIMAL(10,2),
    Subtotal DECIMAL(10,2),
    FOREIGN KEY (Invoice_ID) REFERENCES Invoice(Invoice_ID),
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)
);

CREATE TABLE IF NOT EXISTS Payment (
    Payment_ID INT PRIMARY KEY AUTO_INCREMENT,
    Shop_ID INT,
    Invoice_ID INT,
    Payment_Date DATE,
    Amount_Paid DECIMAL(10,2),
    Payment_Method VARCHAR(50),
    FOREIGN KEY (Shop_ID) REFERENCES Shop(Shop_ID),
    FOREIGN KEY (Invoice_ID) REFERENCES Invoice(Invoice_ID)
);

CREATE TABLE IF NOT EXISTS Purchase_History (
    Purchase_ID INT PRIMARY KEY AUTO_INCREMENT,
    Supplier_ID INT,
    Purchase_Date DATE,
    Total_Amount DECIMAL(10,2),
    Notes TEXT,
    FOREIGN KEY (Supplier_ID) REFERENCES Supplier(Supplier_ID)
);

CREATE TABLE IF NOT EXISTS Purchase_Details (
    Purchase_Detail_ID INT PRIMARY KEY AUTO_INCREMENT,
    Purchase_ID INT,
    Product_ID INT,
    Quantity INT,
    Unit_Cost DECIMAL(10,2),
    Subtotal DECIMAL(10,2),
    FOREIGN KEY (Purchase_ID) REFERENCES Purchase_History(Purchase_ID),
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)
);

INSERT INTO User (Username, Password, First_Name, Last_Name, Email, User_Type, Access_Level) VALUES
('admin', 'admin123', 'Ahmad', 'Salem', 'ahmad@icell.com', 'Owner', 5),
('manager1', 'mgr123', 'Mohammed', 'Ali', 'mohammed@icell.com', 'Manager', 4),
('emp1', 'emp123', 'Sami', 'Hassan', 'sami@icell.com', 'Employee', 2),
('client1', 'cli123', 'Fatima', 'Omar', 'fatima@shop.com', 'Client', 1);

INSERT INTO Warehouse (Warehouse_Name, City, Address, Capacity) VALUES
('Main Warehouse', 'Ramallah', 'Main Street 123', 10000),
('Nablus Warehouse', 'Nablus', 'Commerce St 45', 5000),
('Hebron Warehouse', 'Hebron', 'Industrial Zone 7', 7500);

INSERT INTO Supplier (Supplier_Name, Phone_Number, Email, Country) VALUES
('Samsung', '+82-2-2255-0114', 'sales@samsung.com', 'South Korea'),
('Apple', '+1-800-275-2273', 'sales@apple.com', 'USA'),
('Xiaomi', '+86-10-6060-6666', 'sales@xiaomi.com', 'China'),
('Huawei', '+86-755-2878-0808', 'sales@huawei.com', 'China');

INSERT INTO Product (Product_Name, Brand, Model, Color, RAM, Storage, Last_Purchase_Price, Selling_Price, Supplier_ID) VALUES
('Galaxy S24 Ultra', 'Samsung', 'S24 Ultra', 'Black', '12GB', '256GB', 3500.00, 4200.00, 1),
('Galaxy S24', 'Samsung', 'S24', 'White', '8GB', '128GB', 2800.00, 3400.00, 1),
('iPhone 15 Pro Max', 'Apple', '15 Pro Max', 'Silver', '8GB', '512GB', 5000.00, 6000.00, 2),
('iPhone 15', 'Apple', '15', 'Blue', '6GB', '256GB', 3500.00, 4200.00, 2),
('Xiaomi 14 Pro', 'Xiaomi', '14 Pro', 'Green', '12GB', '512GB', 2500.00, 3000.00, 3),
('Redmi Note 13', 'Xiaomi', 'Note 13', 'Blue', '8GB', '256GB', 800.00, 1100.00, 3),
('Mate 60 Pro', 'Huawei', 'Mate 60 Pro', 'Black', '12GB', '512GB', 3200.00, 3900.00, 4);

INSERT INTO Shop (Shop_Name, Owner_Name, City, Street, User_ID) VALUES
('Mobile World', 'Fatima Omar', 'Ramallah', 'Al-Manara Square', 4),
('Tech Store', 'Khalil Ahmad', 'Nablus', 'Rafidia Street', NULL),
('Smart Cell', 'Rana Yousef', 'Hebron', 'Ein Sara Street', NULL);

INSERT INTO Employee (User_ID, Job_Title, Salary, City, Street, Date_Of_Employment) VALUES
(2, 'Manager', 5000.00, 'Ramallah', 'Main St', '2023-01-15'),
(3, 'Warehouse Employee', 2500.00, 'Ramallah', 'Side St', '2023-06-20');

INSERT INTO Employee_Warehouse_Assignment (Employee_ID, Warehouse_ID, Assignment_Date) VALUES
(1, 1, '2023-01-15'),
(2, 1, '2023-06-20'),
(2, 2, '2024-01-10');

INSERT INTO Inventory (Product_ID, Warehouse_ID, Quantity) VALUES
(1, 1, 50),
(2, 1, 75),
(3, 1, 30),
(4, 1, 45),
(5, 2, 60),
(6, 2, 100),
(7, 3, 40);

INSERT INTO Invoice (Invoice_Date, Shop_ID, Warehouse_ID, Created_By, Total_Amount, Approval_Status, Payment_Status) VALUES
('2025-01-15', 1, 1, 2, 12600.00, 'Approved', 'Paid'),
('2025-02-10', 2, 1, 2, 8400.00, 'Approved', 'Unpaid'),
('2025-02-20', 3, 2, 2, 6000.00, 'Pending', 'Unpaid');

INSERT INTO Invoice_Details (Invoice_ID, Product_ID, Quantity, Unit_Price, Subtotal) VALUES
(1, 1, 3, 4200.00, 12600.00),
(2, 2, 2, 3400.00, 6800.00),
(2, 6, 2, 800.00, 1600.00),
(3, 5, 2, 3000.00, 6000.00);

INSERT INTO Payment (Shop_ID, Invoice_ID, Payment_Date, Amount_Paid, Payment_Method) VALUES
(1, 1, '2025-01-20', 12600.00, 'Bank Transfer');

INSERT INTO Purchase_History (Supplier_ID, Purchase_Date, Total_Amount, Notes) VALUES
(1, '2024-12-01', 175000.00, 'Q4 stock'),
(2, '2024-12-15', 250000.00, 'New iPhone launch');

INSERT INTO Purchase_Details (Purchase_ID, Product_ID, Quantity, Unit_Cost, Subtotal) VALUES
(1, 1, 50, 3500.00, 175000.00),
(2, 3, 50, 5000.00, 250000.00);

SELECT 'Users' AS TableName, COUNT(*) AS Count FROM User
UNION SELECT 'Warehouses', COUNT(*) FROM Warehouse
UNION SELECT 'Suppliers', COUNT(*) FROM Supplier
UNION SELECT 'Products', COUNT(*) FROM Product
UNION SELECT 'Inventory', COUNT(*) FROM Inventory
UNION SELECT 'Shops', COUNT(*) FROM Shop
UNION SELECT 'Employees', COUNT(*) FROM Employee
UNION SELECT 'Invoices', COUNT(*) FROM Invoice
UNION SELECT 'Invoice_Details', COUNT(*) FROM Invoice_Details
UNION SELECT 'Payments', COUNT(*) FROM Payment
UNION SELECT 'Purchase_History', COUNT(*) FROM Purchase_History
UNION SELECT 'Purchase_Details', COUNT(*) FROM Purchase_Details;