import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JdbcDemo {

   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/ComicStore";
   static final String USER = "root";
   static final String PASS = "Linuxnerd1$";

   public static void main(String[] args) {

      Connection conn = null;
      Statement stmt = null;
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         stmt = conn.createStatement();
         conn.setAutoCommit(false);
         Scanner scan = new Scanner(System.in); // Create a Scanner object

         clearScreen();
         System.out.println("\nWELCOME TO COMIC STORE MANAGEMENT SERVICE\n");
         main_menu(stmt, scan,conn);

         scan.close();
         stmt.close();
         conn.close();
      } catch (SQLException se) {
        try{
         if(conn!=null)
             conn.rollback();
             System.out.println("Rolling Back.....");
      }catch(SQLException se2){
	      System.out.println("Rollback failed....");
              se2.printStackTrace();
      }
         // se.printStackTrace();
      } catch (Exception e) { 
         // e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            // se.printStackTrace();
         }
      }
   }

   static void main_menu(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      System.out.println("Login as a ");
      System.out.println("1. Customer");
      System.out.println("2. Comicseller");
      System.out.println("3. Super Admin");
      System.out.println("0. Exit");

      System.out.print("\n\nENTER YOUR CHOICE : ");
      int choice = Integer.parseInt(scan.nextLine());
      clearScreen();

      switch (choice) {
         case 0:
            System.out.println("\nThank you for using our Comic Store Management System!!\n\n");
            System.exit(0);
         case 1:
            check_customer(stmt, scan,conn);
            conn.commit();
            //customer_menu(stmt, scan);
            break;
         case 2:
            check_comicseller(stmt, scan, conn);
            conn.commit();
            break;
         case 3:
            check_super_admin(stmt, scan, conn);
            conn.commit();
            break;
         default:
            clearScreen();
            System.out.println("Please Enter a Valid Choice!!\n");
            break;
      }
      main_menu(stmt, scan,conn);
   }

   static void customer_menu(Statement stmt, Scanner scan,String rollno,Connection conn) throws SQLException {
      System.out.println("Please select appropriate option-");
      System.out.println("1. List of available comics");
      System.out.println("2.List of all comic u have been issued");
      //System.out.println("3.return a comic");
      System.out.println("0. Exit");

      System.out.print("\n\nENTER YOUR CHOICE : ");
      int choice = Integer.parseInt(scan.nextLine());
      System.out.println("Your choice :"+choice);
      clearScreen();
      

      switch (choice) {
         case 0:
            return;
         case 1:
            list_of_comics(stmt, scan, true);
            conn.commit();
            break;
         case 2:
            list_of_comics_of_customer(stmt, scan,rollno);
            conn.commit();
            break;
         //case 3:
            
         default:
            clearScreen();
            System.out.println("Please Enter a Valid Choice!!\n");
            break;
      }
      customer_menu(stmt, scan,rollno,conn);
   }

   static boolean authentication(Statement stmt, Scanner scan, boolean isSuperAdmin) throws SQLException {
      System.out.print("Enter your ID: ");
      String id = scan.nextLine();
      System.out.print("Enter your password: ");
      String password = scan.nextLine();

      clearScreen();
      boolean authenticated = false;

      if (isSuperAdmin) {
         String sql = "SELECT * from super_admin";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try {
            while (rs.next()) {
               String possible_id = rs.getString("super_admin_id");
               String possible_password = rs.getString("super_admin_password");

               if (possible_id.equals(id) && password.equals(possible_password)) {
                  authenticated = true;
                  break;
               }
            }
         } catch (SQLException se) {
         }
      } else {
         String sql = "SELECT * from comicseller";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try {
            while (rs.next()) {
               String possible_id = rs.getString("comicseller_id");
               String possible_password = rs.getString("comicseller_password");

               if (possible_id.equals(id) && password.equals(possible_password)) {
                  authenticated = true;
                  break;
               }
            }
         } catch (SQLException se) {
         }
      }

      return authenticated;
   }
   
   static String authentication_customer(Statement stmt, Scanner scan, boolean isSuperAdmin) throws SQLException {
      System.out.print("Enter your roll number: ");
      String id = scan.nextLine();
      //System.out.print("Enter your password: ");
      //String password = scan.nextLine();

      clearScreen();
      String authenticated = "-1";

      if (isSuperAdmin) {
         String sql = "SELECT * from super_admin";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try {
            while (rs.next()) {
               String possible_id = rs.getString("super_admin_id");
               //String possible_password = rs.getString("super_admin_password");

               if (possible_id.equals(id) /*&& password.equals(possible_password)*/) {
                  authenticated = id;
                  break;
               }
            }
         } catch (SQLException se) {
         }
      } else {
         String sql = "SELECT * from customer";
         ResultSet rs = executeSqlStmt(stmt, sql);

         try {
            while (rs.next()) {
               String possible_id = rs.getString("roll_number");
               //String possible_password = rs.getString("comicseller_password");

               if (possible_id.equals(id) /*&& password.equals(possible_password)*/) {
                  authenticated = id;
                  System.out.println("authenticated");
                  break;
               }
            }
         } catch (SQLException se) {
         }
      }

      return authenticated;
   }

   static void check_customer(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      String result = authentication_customer(stmt, scan, false);
      System.out.println("result variable is :" + result);
      if (!result.equals("-1")) {
         customer_menu(stmt, scan,result,conn);
      } else {
         System.out.print("Entered details were incorrect. Do you want to try again (Y/N)? ");
         String input = scan.nextLine();
         if (input.equals("Y"))
            check_customer(stmt, scan,conn);
         else
            return;
      }
   }

   static void check_comicseller(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      if (authentication(stmt, scan, false)) {
         comicseller_menu(stmt, scan, conn);
      } else {
         System.out.print("Entered details were incorrect. Do you want to try again (Y/N)? ");
         String input = scan.nextLine();
         if (input.equals("Y"))
            check_comicseller(stmt, scan, conn);
         else
            return;
      }
   }

   static void check_super_admin(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      if (authentication(stmt, scan, true)) {
         super_admin_menu(stmt, scan, conn);
      } else {
         System.out.print("Entered details were incorrect. Do you want to try again (Y/N)? ");
         String input = scan.nextLine();
         if (input.equals("Y"))
            check_super_admin(stmt, scan,conn);
         else
            return;
      }
   }

   static void comicseller_menu(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      System.out.println("Please select appropriate option-");
      System.out.println("1. List of all comics");
      System.out.println("2. List of available comics");
      System.out.println("3. Issue a comic");
      System.out.println("4. Return a comic");
      System.out.println("5. Add a comic");
      System.out.println("6. Delete a comic");
      System.out.println("0. Exit");

      System.out.print("\n\nENTER YOUR CHOICE : ");
      int choice = Integer.parseInt(scan.nextLine());
      clearScreen();

      switch (choice) {
         case 0:
            return;
         case 1:
            list_of_comics(stmt, scan, false);
            conn.commit();
            break;
         case 2:
            list_of_comics(stmt, scan, true);
            conn.commit();
            break;
         case 3:
            issue_comic(stmt, scan, conn);
            //conn.commit();
            break;
         case 4:
            return_comic(stmt, scan);
            conn.commit();
            break;
         case 5:
            add_comic(stmt, scan);
            conn.commit();
            break;
         case 6:
            delete_comic(stmt, scan);
            conn.commit();
            break;
         default:
            clearScreen();
            System.out.println("Please Enter a Valid Choice!!\n");
            break;
      }
      comicseller_menu(stmt, scan,conn);
   }

   static void super_admin_menu(Statement stmt, Scanner scan, Connection conn) throws SQLException {
      System.out.println("Please select appropriate option-");
      System.out.println("1. List of customer");
      System.out.println("2. List of comicsellers");
      System.out.println("3. Add a customer");
      System.out.println("4. Delete a customer");
      System.out.println("5. Add a comicseller");
      System.out.println("6. Delete a comicseller");
      System.out.println("0. Exit");

      System.out.print("\n\nENTER YOUR CHOICE : ");
      int choice = Integer.parseInt(scan.nextLine());
      clearScreen();

      switch (choice) {
         case 0:
            return;
         case 1:
            list_of_customers(stmt, scan);
            conn.commit();
            break;
         case 2:
            list_of_comicsellers(stmt, scan);
            conn.commit();
            break;
         case 3:
            add_customer(stmt, scan);
            conn.commit();
            break;
         case 4:
            delete_customer(stmt, scan);
            conn.commit();
            break;
         case 5:
            add_comicseller(stmt, scan);
            conn.commit();
            break;
         case 6:
            delete_comicseller(stmt, scan);
            conn.commit();
            break;
         default:
            clearScreen();
            System.out.println("Please Enter a Valid Choice!!\n");
            break;
      }
      super_admin_menu(stmt, scan,conn);
   }

   static boolean list_of_comics(Statement stmt, Scanner scan, boolean checkAvailable) throws SQLException {
      String sql = "select * from comic";
      ResultSet rs = executeSqlStmt(stmt, sql);
      boolean nocomics = true;

      try {
         System.out.println("List of available comics:\n");
         while (rs.next()) {
            String id = rs.getString("comic_id");
            String name = rs.getString("comic_name");
            String author = rs.getString("comic_author");
            Integer year = rs.getInt("publication_year");
            String issuer = rs.getString("issuer");

            if (checkAvailable) {
               if (issuer == null) {
                  System.out.println("ID : " + id);
                  System.out.println("comic Name: " + name);
                  System.out.println("Author : " + author);
                  System.out.println("Publication year : " + year);
                  System.out.println("");
                  nocomics = false;
               }
            } else {
               System.out.println("ID : " + id);
               System.out.println("comic Name: " + name);
               System.out.println("Author : " + author);
               System.out.println("Publication year : " + year);
               System.out.println("Issuer : " + issuer);
               System.out.println("");
               nocomics = false;
            }
         }

         if (nocomics)
            System.out.println("Sorry, no comics are available!\n");

         rs.close();
      } catch (SQLException e) {
         // e.printStackTrace();
      }
      return nocomics;
   }

   //list_of_comics_of_customer(stmt, scan,rollno)
   static boolean list_of_comics_of_customer(Statement stmt, Scanner scan, String rollno) throws SQLException {
      //String sql = "select * from transactions where roll_number = %s";
      //String sql = String.format("SELECT comic_id from transaction WHERE roll_number = '%s'", rollno);
      String sql = String.format("SELECT * from comic b JOIN transaction t ON b.comic_id = t.comic_id WHERE t.roll_number = '%s'", rollno);
      ResultSet rs = executeSqlStmt(stmt, sql);
      boolean nocomics = true;

      try {
         System.out.println("List of comics taken by you:\n");
         while (rs.next()) {
            String id = rs.getString("comic_id");
            String name = rs.getString("comic_name");
            String author = rs.getString("comic_author");
            Integer year = rs.getInt("publication_year");
            //String issuer = rs.getString("issuer");
            //System.out.println(id + " ");
            //if (checkAvailable) {
              // if (issuer == null) {
                  System.out.println("ID : " + id);
                  System.out.println("comic Name: " + name);
                  System.out.println("Author : " + author);
                  System.out.println("Publication year : " + year);
                  System.out.println("");
                  //nocomics = false;*/
               //}
            /* } else {
               System.out.println("ID : " + id);
               System.out.println("comic Name: " + name);
               System.out.println("Author : " + author);
               System.out.println("Publication year : " + year);
               System.out.println("Issuer : " + issuer);
               System.out.println("");
               nocomics = false;
            }
         }*/
            nocomics = false;
         }

         if (nocomics)
            System.out.println("Sorry, no comics are taken by you!\n");
         rs.close();
      } catch (SQLException e) {
         // e.printStackTrace();
      }
      return nocomics;
   }

   static void issue_comic(Statement stmt, Scanner scan, Connection conn) {
      try {
          boolean nocomics = list_of_comics(stmt, scan, true);
          if (!nocomics) {
              System.out.print("\nEnter comic ID : ");
              String id = scan.nextLine();
  
              System.out.print("Enter customer roll number : ");
              String customer_roll_no = scan.nextLine();
  
              clearScreen();
  
              // Check if the customer roll number exists
            //   if (!checkCustomerExistence(stmt, customer_roll_no)) {
            //       System.out.println("Customer with roll number " + customer_roll_no + " does not exist.");
            //       conn.rollback();
            //       System.out.println("Rolling back.....");
            //       return;
            //   }
  
              String sql = String.format("UPDATE comic SET issuer = '%s' WHERE comic_id = '%s'", customer_roll_no, id);
              //conn.setAutoCommit(false);
              int result = updateSqlStmt(stmt, sql);
  
              if (result != 0) {
                  System.out.println("ISSUER HAS BEEN UPDATED SUCCESSFULLY!!");
                  String sql1 = String.format("INSERT INTO transaction VALUES('%s', '%s')", customer_roll_no, id);
                  int result1 = updateSqlStmt(stmt, sql1);
                  if (result1 == 0) {
                      System.out.println("Transaction insertion failed.");
                      conn.rollback();
                  } else {
                      conn.commit();
                  }
              } else {
                  System.out.println("Rolling back...");
                  conn.rollback();
              }
          }
      } catch (SQLException e) {
          //e.printStackTrace();
          try {
              System.out.println("Rolling back...");
              conn.rollback();
          } catch (SQLException se) {
              se.printStackTrace();
          }
      }
  }
  
  static boolean checkCustomerExistence(Statement stmt, String rollNumber) throws SQLException {
      String sql = String.format("SELECT * FROM customer WHERE roll_number = '%s'", rollNumber);
      ResultSet rs = executeSqlStmt(stmt, sql);
      boolean exists = rs.next();
      rs.close();
      return exists;
  }
  
   static void return_comic(Statement stmt, Scanner scan) {
      try {
         System.out.print("\nEnter comic ID : ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format("UPDATE comic SET issuer = NULL WHERE comic_id = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("comic has been returned!!\n");
            String sql1 = String.format("DELETE FROM transaction where comic_id = '%s'", id);
               int result1 = updateSqlStmt(stmt, sql1);
               if(result1==0){
                  System.out.println("transaction deletion failed");
               }
      } catch (Exception e) {
         // e.printStackTrace();
      }
   }

   static void add_comic(Statement stmt, Scanner scan) throws SQLException {
      // try {2
         System.out.print("\nEnter comic ID : ");
         String id = scan.nextLine();
         System.out.print("\nEnter comic name : ");
         String name = scan.nextLine();
         System.out.print("\nEnter comic author : ");
         String author = scan.nextLine();
         System.out.print("\nEnter comic publication year : ");
         Integer year = Integer.parseInt(scan.nextLine());

         clearScreen();

         String sql = String.format(
               "INSERT INTO comic VALUES('%s', '%s', '%s', '%d', NULL);",
               id, name, author, year);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("comic has been added successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      // } catch (Exception e) {
         // e.printStackTrace();
      // }
   }

   static void delete_comic(Statement stmt, Scanner scan) {
      try {
         System.out.print("\nEnter comic ID : ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format(
               "DELETE FROM comic where comic_id = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("comic has been deleted successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      } catch (Exception e) {
         // e.printStackTrace();
      }
   }

   static void list_of_customers(Statement stmt, Scanner scan) throws SQLException {
      String sql = "select * from customer";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try {
         System.out.println("List of customers:\n");
         while (rs.next()) {
            String id = rs.getString("roll_number");
            String name = rs.getString("full_name");

            System.out.println("Roll number : " + id);
            System.out.println("Full Name: " + name);
            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         // e.printStackTrace();
      }
   }

   static void list_of_comicsellers(Statement stmt, Scanner scan) throws SQLException {
      String sql = "select * from comicseller";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try {
         System.out.println("List of comicsellers:\n");
         while (rs.next()) {
            String id = rs.getString("comicseller_id");
            String name = rs.getString("comicseller_name");

            System.out.println("Roll number : " + id);
            System.out.println("Full Name: " + name);
            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         // e.printStackTrace();
      }
   }

   static void add_customer(Statement stmt, Scanner scan) throws SQLException {
      //try {
         System.out.print("Enter customer roll number : ");
         String id = scan.nextLine();
         System.out.print("Enter customer name : ");
         String name = scan.nextLine();

         clearScreen();

         String sql = String.format("INSERT INTO customer VALUES('%s', '%s', NULL)", id, name);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("customer has been added successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      } //catch (Exception e) {
         // e.printStackTrace();
      //}
   //}

   static void add_comicseller(Statement stmt, Scanner scan) {
      try {
         System.out.print("Enter comicseller id : ");
         String id = scan.nextLine();
         System.out.print("Enter comicseller name : ");
         String name = scan.nextLine();
         System.out.print("Enter comicseller password : ");
         String password = scan.nextLine();

         clearScreen();

         String sql = String.format("INSERT INTO comicseller VALUES('%s', '%s', '%s')", id, name, password);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("comicseller has been added successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      } catch (Exception e) {
         // e.printStackTrace();
      }
   }

   static void delete_customer(Statement stmt, Scanner scan) {
      try {
         System.out.print("Enter customer roll number : ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format("DELETE FROM customer where roll_number = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("customer has been deleted successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      } catch (Exception e) {
         // e.printStackTrace();
      }
   }

   static void delete_comicseller(Statement stmt, Scanner scan) {
      try {
         System.out.print("Enter comicseller id : ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format("DELETE FROM comicseller where comicseller_id = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0)
            System.out.println("comicseller has been deleted successfully!!\n");
         else
            System.out.println("Something went wrong!\n");
      } catch (Exception e) {
         // e.printStackTrace();
      }
   }

   static ResultSet executeSqlStmt(Statement stmt, String sql) throws SQLException{
      // try {
         ResultSet rs = stmt.executeQuery(sql);
         return rs;
      // } catch (SQLException se) {
         // se.printStackTrace();
      // } catch (Exception e) {
         // e.printStackTrace();
      // }
      // return null;
   }

   static int updateSqlStmt(Statement stmt, String sql) throws SQLException {
      // try {
         int rs = stmt.executeUpdate(sql);
         return rs;
      // } catch (SQLException se) {
      //    // se.printStackTrace();
      // } catch (Exception e) {
      //    // e.printStackTrace();
      // }
      // return 0;
   }

   static void clearScreen() {
      System.out.println("\033[H\033[J");
      System.out.flush();
   }
}