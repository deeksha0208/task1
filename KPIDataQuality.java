import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/*
 * ============================================================
 * KPI DICTIONARY & DATA QUALITY CONTRACT
 * E-COMMERCE SALES ANALYTICS PROJECT
 * ============================================================
 *
 * Business Owner  : Head of Sales
 * Data Owner      : Data Analytics Team
 * Refresh Cadence : Daily
 *
 * KPIs:
 * 1. Total Revenue
 * 2. Total Orders
 * 3. Average Order Value
 * 4. Units Sold
 * 5. Unique Customers
 * 6. Cancellation Rate
 * 7. Revenue per Customer
 * 8. Orders per Customer
 * 9. Repeat Customer Rate
 * 10. Top Product Revenue
 *
 * Data Quality:
 * 1. Completeness
 * 2. Uniqueness
 * 3. Validity
 * 4. Consistency
 * 5. Freshness
 * ============================================================
 */

public class KPIDataQuality {

    // =========================================================
    // DATA RECORD
    // =========================================================

    static class Sale {

        String orderId;
        LocalDate orderDate;
        String customerId;
        String productId;
        String productName;
        int quantity;
        double unitPrice;
        double revenue;
        String status;
        String country;

        Sale(
                String orderId,
                LocalDate orderDate,
                String customerId,
                String productId,
                String productName,
                int quantity,
                double unitPrice,
                double revenue,
                String status,
                String country) {

            this.orderId = orderId;
            this.orderDate = orderDate;
            this.customerId = customerId;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.revenue = revenue;
            this.status = status;
            this.country = country;
        }
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        System.out.println();
        System.out.println("============================================================");
        System.out.println("       KPI DICTIONARY & DATA QUALITY CONTRACT");
        System.out.println("             E-COMMERCE SALES ANALYTICS");
        System.out.println("============================================================");

        String fileName = "ecommerce_sales.csv";

        File file = new File(fileName);

        // Create sample CSV if it doesn't exist
        if (!file.exists()) {

            System.out.println("\nDataset not found.");
            System.out.println("Creating sample ecommerce_sales.csv...");

            createSampleCSV(fileName);

            System.out.println("Sample dataset created successfully.");
        }

        // Load data
        ArrayList<Sale> sales = readCSV(fileName);

        if (sales.isEmpty()) {

            System.out.println("No data available.");
            return;
        }

        System.out.println("\nRecords loaded: " + sales.size());

        // =====================================================
        // KPI CALCULATIONS
        // =====================================================

        double totalRevenue = calculateTotalRevenue(sales);

        int totalOrders = calculateTotalOrders(sales);

        double averageOrderValue =
                totalOrders == 0
                        ? 0
                        : totalRevenue / totalOrders;

        int unitsSold = calculateUnitsSold(sales);

        int uniqueCustomers =
                calculateUniqueCustomers(sales);

        int cancelledOrders =
                calculateCancelledOrders(sales);

        int allOrders =
                calculateAllOrders(sales);

        double cancellationRate =
                allOrders == 0
                        ? 0
                        : ((double) cancelledOrders / allOrders) * 100;

        double revenuePerCustomer =
                uniqueCustomers == 0
                        ? 0
                        : totalRevenue / uniqueCustomers;

        double ordersPerCustomer =
                uniqueCustomers == 0
                        ? 0
                        : (double) totalOrders / uniqueCustomers;

        double repeatCustomerRate =
                calculateRepeatCustomerRate(sales);

        String topProduct =
                findTopProduct(sales);

        double topProductRevenue =
                findTopProductRevenue(sales);

        // =====================================================
        // DISPLAY KPI RESULTS
        // =====================================================

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                     KPI RESULTS");
        System.out.println("============================================================");

        System.out.printf(
                "Total Revenue          : ₹%.2f%n",
                totalRevenue);

        System.out.println(
                "Total Orders           : " + totalOrders);

        System.out.printf(
                "Average Order Value    : ₹%.2f%n",
                averageOrderValue);

        System.out.println(
                "Units Sold             : " + unitsSold);

        System.out.println(
                "Unique Customers       : " + uniqueCustomers);

        System.out.println(
                "Cancelled Orders       : " + cancelledOrders);

        System.out.printf(
                "Cancellation Rate      : %.2f%%%n",
                cancellationRate);

        System.out.printf(
                "Revenue per Customer   : ₹%.2f%n",
                revenuePerCustomer);

        System.out.printf(
                "Orders per Customer    : %.2f%n",
                ordersPerCustomer);

        System.out.printf(
                "Repeat Customer Rate   : %.2f%%%n",
                repeatCustomerRate);

        System.out.println(
                "Top Product            : " + topProduct);

        System.out.printf(
                "Top Product Revenue    : ₹%.2f%n",
                topProductRevenue);

        // =====================================================
        // DATA QUALITY CHECKS
        // =====================================================

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                  DATA QUALITY CHECKS");
        System.out.println("============================================================");

        int failedChecks = 0;

        // -----------------------------------------------------
        // COMPLETENESS
        // -----------------------------------------------------

        int missingValues = checkCompleteness(sales);

        double missingPercentage =
                ((double) missingValues /
                        (sales.size() * 7)) * 100;

        boolean completenessPass =
                missingPercentage < 2;

        System.out.println("\n1. COMPLETENESS");

        System.out.println(
                "Missing values     : " + missingValues);

        System.out.printf(
                "Missing percentage : %.2f%%%n",
                missingPercentage);

        System.out.println(
                "Threshold          : < 2%");

        System.out.println(
                "Status             : " +
                        (completenessPass ? "PASS" : "FAIL"));

        if (!completenessPass)
            failedChecks++;

        // -----------------------------------------------------
        // UNIQUENESS
        // -----------------------------------------------------

        int duplicateRecords =
                checkUniqueness(sales);

        boolean uniquenessPass =
                duplicateRecords == 0;

        System.out.println("\n2. UNIQUENESS");

        System.out.println(
                "Duplicate records  : " + duplicateRecords);

        System.out.println(
                "Threshold          : 0 duplicates");

        System.out.println(
                "Status             : " +
                        (uniquenessPass ? "PASS" : "FAIL"));

        if (!uniquenessPass)
            failedChecks++;

        // -----------------------------------------------------
        // VALIDITY
        // -----------------------------------------------------

        int invalidRecords =
                checkValidity(sales);

        double validityPercentage =
                ((double) (sales.size() - invalidRecords)
                        / sales.size()) * 100;

        boolean validityPass =
                validityPercentage >= 99.5;

        System.out.println("\n3. VALIDITY");

        System.out.println(
                "Invalid records    : " + invalidRecords);

        System.out.printf(
                "Valid percentage   : %.2f%%%n",
                validityPercentage);

        System.out.println(
                "Threshold          : >= 99.5%");

        System.out.println(
                "Status             : " +
                        (validityPass ? "PASS" : "FAIL"));

        if (!validityPass)
            failedChecks++;

        // -----------------------------------------------------
        // CONSISTENCY
        // -----------------------------------------------------

        int inconsistentRecords =
                checkConsistency(sales);

        double consistencyPercentage =
                ((double) (sales.size() - inconsistentRecords)
                        / sales.size()) * 100;

        boolean consistencyPass =
                consistencyPercentage >= 99.5;

        System.out.println("\n4. CONSISTENCY");

        System.out.println(
                "Inconsistent records : " +
                        inconsistentRecords);

        System.out.printf(
                "Consistency          : %.2f%%%n",
                consistencyPercentage);

        System.out.println(
                "Threshold            : >= 99.5%");

        System.out.println(
                "Status               : " +
                        (consistencyPass ? "PASS" : "FAIL"));

        if (!consistencyPass)
            failedChecks++;

        // -----------------------------------------------------
        // FRESHNESS
        // -----------------------------------------------------

        long dataAge =
                calculateDataAge(sales);

        boolean freshnessPass =
                dataAge <= 1;

        System.out.println("\n5. FRESHNESS");

        System.out.println(
                "Latest data date    : " +
                        getLatestDate(sales));

        System.out.println(
                "Data age            : " +
                        dataAge + " day(s)");

        System.out.println(
                "Threshold           : <= 1 day");

        System.out.println(
                "Status              : " +
                        (freshnessPass ? "PASS" : "FAIL"));

        if (!freshnessPass)
            failedChecks++;

        // =====================================================
        // OVERALL STATUS
        // =====================================================

        String overallStatus;

        if (failedChecks == 0) {

            overallStatus =
                    "GREEN - DATA IS TRUSTWORTHY";

        } else if (failedChecks <= 2) {

            overallStatus =
                    "AMBER - DATA NEEDS REVIEW";

        } else {

            overallStatus =
                    "RED - DATA QUALITY FAILURE";
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("              OVERALL DATA QUALITY STATUS");
        System.out.println("============================================================");

        System.out.println(overallStatus);

        // =====================================================
        // CREATE REPORTS
        // =====================================================

        createKPIReport(
                totalRevenue,
                totalOrders,
                averageOrderValue,
                unitsSold,
                uniqueCustomers,
                cancellationRate,
                revenuePerCustomer,
                ordersPerCustomer,
                repeatCustomerRate,
                topProduct,
                topProductRevenue
        );

        createQualityReport(
                missingPercentage,
                duplicateRecords,
                validityPercentage,
                consistencyPercentage,
                dataAge,
                overallStatus
        );

        createContractReport(
                overallStatus
        );

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 PROJECT COMPLETED");
        System.out.println("============================================================");

        System.out.println("\nGenerated files:");

        System.out.println("1. ecommerce_sales.csv");
        System.out.println("2. kpi_report.csv");
        System.out.println("3. data_quality_report.csv");
        System.out.println("4. data_quality_contract.txt");

        System.out.println();
    }

    // =========================================================
    // CREATE SAMPLE CSV
    // =========================================================

    static void createSampleCSV(String fileName) {

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(fileName));

            writer.println(
                    "Order_ID,Order_Date,Customer_ID," +
                    "Product_ID,Product_Name,Quantity," +
                    "UnitPrice,Revenue,Status,Country");

            String[][] products = {

                    {"P001", "Laptop", "55000"},
                    {"P002", "Smartphone", "25000"},
                    {"P003", "Headphones", "2500"},
                    {"P004", "Smart Watch", "5000"},
                    {"P005", "Keyboard", "1500"},
                    {"P006", "Mouse", "800"},
                    {"P007", "Tablet", "18000"},
                    {"P008", "Monitor", "12000"}
            };

            Random random = new Random(42);

            for (int i = 1; i <= 500; i++) {

                String[] product =
                        products[random.nextInt(products.length)];

                String orderId =
                        String.format("O%04d", i);

                String customerId =
                        String.format(
                                "C%03d",
                                random.nextInt(100) + 1);

                String productId = product[0];

                String productName = product[1];

                double price =
                        Double.parseDouble(product[2]);

                int quantity =
                        random.nextInt(4) + 1;

                double discount =
                        random.nextInt(3) * 0.05;

                double unitPrice =
                        price * (1 - discount);

                LocalDate date =
                        LocalDate.of(2026, 8, 1)
                                .plusDays(
                                        random.nextInt(24));

                String status;

                if (random.nextInt(4) == 0)
                    status = "Cancelled";
                else
                    status = "Completed";

                double revenue =
                        quantity * unitPrice;

                writer.printf(
                        "%s,%s,%s,%s,%s,%d,%.2f,%.2f,%s,India%n",

                        orderId,
                        date,
                        customerId,
                        productId,
                        productName,
                        quantity,
                        unitPrice,
                        revenue,
                        status
                );
            }

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error creating CSV: " +
                            e.getMessage());
        }
    }

    // =========================================================
    // READ CSV
    // =========================================================

    static ArrayList<Sale> readCSV(
            String fileName) {

        ArrayList<Sale> sales =
                new ArrayList<>();

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(fileName));

            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length < 10)
                    continue;

                try {

                    String orderId = data[0];

                    LocalDate orderDate =
                            LocalDate.parse(data[1]);

                    String customerId = data[2];

                    String productId = data[3];

                    String productName = data[4];

                    int quantity =
                            Integer.parseInt(data[5]);

                    double unitPrice =
                            Double.parseDouble(data[6]);

                    double revenue =
                            Double.parseDouble(data[7]);

                    String status = data[8];

                    String country = data[9];

                    Sale sale =
                            new Sale(
                                    orderId,
                                    orderDate,
                                    customerId,
                                    productId,
                                    productName,
                                    quantity,
                                    unitPrice,
                                    revenue,
                                    status,
                                    country
                            );

                    sales.add(sale);

                } catch (Exception e) {

                    // Skip invalid row
                }
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading CSV: " +
                            e.getMessage());
        }

        return sales;
    }

    // =========================================================
    // KPI 1 - TOTAL REVENUE
    // =========================================================

    static double calculateTotalRevenue(
            ArrayList<Sale> sales) {

        double total = 0;

        for (Sale sale : sales) {

            if (sale.status.equals("Completed")) {

                total += sale.revenue;
            }
        }

        return total;
    }

    // =========================================================
    // KPI 2 - TOTAL ORDERS
    // =========================================================

    static int calculateTotalOrders(
            ArrayList<Sale> sales) {

        HashSet<String> orders =
                new HashSet<>();

        for (Sale sale : sales) {

            if (sale.status.equals("Completed")) {

                orders.add(sale.orderId);
            }
        }

        return orders.size();
    }

    // =========================================================
    // KPI 4 - UNITS SOLD
    // =========================================================

    static int calculateUnitsSold(
            ArrayList<Sale> sales) {

        int units = 0;

        for (Sale sale : sales) {

            if (sale.status.equals("Completed")) {

                units += sale.quantity;
            }
        }

        return units;
    }

    // =========================================================
    // KPI 5 - UNIQUE CUSTOMERS
    // =========================================================

    static int calculateUniqueCustomers(
            ArrayList<Sale> sales) {

        HashSet<String> customers =
                new HashSet<>();

        for (Sale sale : sales) {

            if (sale.status.equals("Completed")) {

                customers.add(
                        sale.customerId);
            }
        }

        return customers.size();
    }

    // =========================================================
    // KPI 6 - CANCELLED ORDERS
    // =========================================================

    static int calculateCancelledOrders(
            ArrayList<Sale> sales) {

        HashSet<String> orders =
                new HashSet<>();

        for (Sale sale : sales) {

            if (sale.status.equals("Cancelled")) {

                orders.add(sale.orderId);
            }
        }

        return orders.size();
    }

    // =========================================================
    // ALL ORDERS
    // =========================================================

    static int calculateAllOrders(
            ArrayList<Sale> sales) {

        HashSet<String> orders =
                new HashSet<>();

        for (Sale sale : sales) {

            orders.add(sale.orderId);
        }

        return orders.size();
    }

    // =========================================================
    // KPI 9 - REPEAT CUSTOMER RATE
    // =========================================================

    static double calculateRepeatCustomerRate(
            ArrayList<Sale> sales) {

        HashMap<String, HashSet<String>>
                customerOrders =
                new HashMap<>();

        for (Sale sale : sales) {

            if (!sale.status.equals("Completed"))
                continue;

            customerOrders
                    .putIfAbsent(
                            sale.customerId,
                            new HashSet<>());

            customerOrders
                    .get(sale.customerId)
                    .add(sale.orderId);
        }

        int repeatCustomers = 0;

        for (HashSet<String> orders :
                customerOrders.values()) {

            if (orders.size() > 1) {

                repeatCustomers++;
            }
        }

        if (customerOrders.isEmpty())
            return 0;

        return ((double) repeatCustomers
                / customerOrders.size()) * 100;
    }

    // =========================================================
    // KPI 10 - TOP PRODUCT
    // =========================================================

    static String findTopProduct(
            ArrayList<Sale> sales) {

        HashMap<String, Double>
                productRevenue =
                new HashMap<>();

        for (Sale sale : sales) {

            if (!sale.status.equals("Completed"))
                continue;

            productRevenue.put(
                    sale.productName,
                    productRevenue.getOrDefault(
                            sale.productName,
                            0.0)
                            + sale.revenue);
        }

        String topProduct = "None";

        double maxRevenue = 0;

        for (Map.Entry<String, Double> entry :
                productRevenue.entrySet()) {

            if (entry.getValue() > maxRevenue) {

                maxRevenue =
                        entry.getValue();

                topProduct =
                        entry.getKey();
            }
        }

        return topProduct;
    }

    // =========================================================
    // TOP PRODUCT REVENUE
    // =========================================================

    static double findTopProductRevenue(
            ArrayList<Sale> sales) {

        HashMap<String, Double>
                productRevenue =
                new HashMap<>();

        for (Sale sale : sales) {

            if (!sale.status.equals("Completed"))
                continue;

            productRevenue.put(
                    sale.productName,
                    productRevenue.getOrDefault(
                            sale.productName,
                            0.0)
                            + sale.revenue);
        }

        double maxRevenue = 0;

        for (double revenue :
                productRevenue.values()) {

            if (revenue > maxRevenue)
                maxRevenue = revenue;
        }

        return maxRevenue;
    }

    // =========================================================
    // COMPLETENESS CHECK
    // =========================================================

    static int checkCompleteness(
            ArrayList<Sale> sales) {

        int missing = 0;

        for (Sale sale : sales) {

            if (sale.orderId == null ||
                    sale.orderId.isEmpty())

                missing++;

            if (sale.customerId == null ||
                    sale.customerId.isEmpty())

                missing++;

            if (sale.productId == null ||
                    sale.productId.isEmpty())

                missing++;

            if (sale.productName == null ||
                    sale.productName.isEmpty())

                missing++;

            if (sale.country == null ||
                    sale.country.isEmpty())

                missing++;
        }

        return missing;
    }

    // =========================================================
    // UNIQUENESS CHECK
    // =========================================================

    static int checkUniqueness(
            ArrayList<Sale> sales) {

        HashSet<String> records =
                new HashSet<>();

        int duplicates = 0;

        for (Sale sale : sales) {

            String key =
                    sale.orderId +
                    "_" +
                    sale.productId;

            if (records.contains(key)) {

                duplicates++;

            } else {

                records.add(key);
            }
        }

        return duplicates;
    }

    // =========================================================
    // VALIDITY CHECK
    // =========================================================

    static int checkValidity(
            ArrayList<Sale> sales) {

        int invalid = 0;

        for (Sale sale : sales) {

            if (sale.quantity <= 0)
                invalid++;

            if (sale.unitPrice < 0)
                invalid++;

            if (sale.orderDate == null)
                invalid++;

            if (!sale.status.equals("Completed") &&
                    !sale.status.equals("Cancelled"))

                invalid++;
        }

        return invalid;
    }

    // =========================================================
    // CONSISTENCY CHECK
    // =========================================================

    static int checkConsistency(
            ArrayList<Sale> sales) {

        int inconsistent = 0;

        for (Sale sale : sales) {

            double expectedRevenue =
                    sale.quantity *
                            sale.unitPrice;

            if (Math.abs(
                    sale.revenue -
                            expectedRevenue) > 0.01) {

                inconsistent++;
            }
        }

        return inconsistent;
    }

    // =========================================================
    // LATEST DATE
    // =========================================================

    static LocalDate getLatestDate(
            ArrayList<Sale> sales) {

        LocalDate latest =
                sales.get(0).orderDate;

        for (Sale sale : sales) {

            if (sale.orderDate.isAfter(latest)) {

                latest =
                        sale.orderDate;
            }
        }

        return latest;
    }

    // =========================================================
    // FRESHNESS CHECK
    // =========================================================

    static long calculateDataAge(
            ArrayList<Sale> sales) {

        LocalDate latest =
                getLatestDate(sales);

        // Project evaluation date
        LocalDate currentDate =
                LocalDate.of(2026, 8, 24);

        return ChronoUnit.DAYS.between(
                latest,
                currentDate);
    }

    // =========================================================
    // CREATE KPI REPORT
    // =========================================================

    static void createKPIReport(
            double revenue,
            int orders,
            double aov,
            int units,
            int customers,
            double cancellationRate,
            double revenuePerCustomer,
            double ordersPerCustomer,
            double repeatRate,
            String topProduct,
            double topProductRevenue) {

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(
                                    "kpi_report.csv"));

            writer.println(
                    "KPI,Value");

            writer.printf(
                    "Total Revenue,%.2f%n",
                    revenue);

            writer.println(
                    "Total Orders," + orders);

            writer.printf(
                    "Average Order Value,%.2f%n",
                    aov);

            writer.println(
                    "Units Sold," + units);

            writer.println(
                    "Unique Customers," +
                            customers);

            writer.printf(
                    "Cancellation Rate,%.2f%%%n",
                    cancellationRate);

            writer.printf(
                    "Revenue per Customer,%.2f%n",
                    revenuePerCustomer);

            writer.printf(
                    "Orders per Customer,%.2f%n",
                    ordersPerCustomer);

            writer.printf(
                    "Repeat Customer Rate,%.2f%%%n",
                    repeatRate);

            writer.println(
                    "Top Product," +
                            topProduct);

            writer.printf(
                    "Top Product Revenue,%.2f%n",
                    topProductRevenue);

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error creating KPI report.");
        }
    }

    // =========================================================
    // CREATE QUALITY REPORT
    // =========================================================

    static void createQualityReport(
            double completeness,
            int duplicates,
            double validity,
            double consistency,
            long freshness,
            String status) {

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(
                                    "data_quality_report.csv"));

            writer.println(
                    "Dimension,Result,Threshold,Status");

            writer.printf(
                    "Completeness,%.2f%%,< 2%%,%s%n",
                    completeness,
                    completeness < 2
                            ? "PASS"
                            : "FAIL");

            writer.printf(
                    "Uniqueness,%d,0 duplicates,%s%n",
                    duplicates,
                    duplicates == 0
                            ? "PASS"
                            : "FAIL");

            writer.printf(
                    "Validity,%.2f%%,>= 99.5%%,%s%n",
                    validity,
                    validity >= 99.5
                            ? "PASS"
                            : "FAIL");

            writer.printf(
                    "Consistency,%.2f%%,>= 99.5%%,%s%n",
                    consistency,
                    consistency >= 99.5
                            ? "PASS"
                            : "FAIL");

            writer.printf(
                    "Freshness,%d days,<= 1 day,%s%n",
                    freshness,
                    freshness <= 1
                            ? "PASS"
                            : "FAIL");

            writer.println(
                    "Overall Status," +
                            status);

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error creating quality report.");
        }
    }

    // =========================================================
    // CREATE DATA QUALITY CONTRACT
    // =========================================================

    static void createContractReport(
            String status) {

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(
                                    "data_quality_contract.txt"));

            writer.println(
                    "KPI DICTIONARY & DATA QUALITY CONTRACT");

            writer.println(
                    "======================================");

            writer.println();

            writer.println(
                    "Dataset: E-Commerce Sales Dataset");

            writer.println(
                    "Business Owner: Head of Sales");

            writer.println(
                    "Data Owner: Data Analytics Team");

            writer.println(
                    "Refresh Cadence: Daily");

            writer.println();

            writer.println(
                    "DATA QUALITY THRESHOLDS");

            writer.println(
                    "------------------------");

            writer.println(
                    "Completeness: Less than 2% missing");

            writer.println(
                    "Uniqueness: 0 unintended duplicates");

            writer.println(
                    "Validity: At least 99.5% valid");

            writer.println(
                    "Consistency: At least 99.5% consistent");

            writer.println(
                    "Freshness: Data must be less than or equal to 24 hours old");

            writer.println();

            writer.println(
                    "ESCALATION POLICY");

            writer.println(
                    "-----------------");

            writer.println(
                    "GREEN: Publish dashboard normally.");

            writer.println(
                    "AMBER: Notify data owner and investigate.");

            writer.println(
                    "RED: Flag affected reports, notify business owner, " +
                    "fix the data and rerun quality checks.");

            writer.println();

            writer.println(
                    "CURRENT OVERALL STATUS:");

            writer.println(status);

            writer.close();

        } catch (IOException e) {

            System.out.println(
                    "Error creating contract.");
        }
    }
}