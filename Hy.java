package home;

import java.util.Scanner;
import java.sql.*;

public class Hy {

    public static void main(String[] args) {

        int choice;
        String phone;
        String bookname;
        String mname;
        String mail;
        int fine = 0;
        int day;

        Scanner sc = new Scanner(System.in);

        try {

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    "AshishMySQL#@3892");

            System.out.println("1. Register new member");
            System.out.println("2. View available books");
            System.out.println("3. Issue books");
            System.out.println("4. Return book");
            System.out.println("Enter option number");

            choice = sc.nextInt();

            switch (choice) {

            case 1:

                sc.nextLine();

                System.out.println("Register new member option selected");

                System.out.println("Enter Member ID");
                int memberid = sc.nextInt();
                sc.nextLine();

                // Check duplicate Member ID
                PreparedStatement checkMember =
                        con.prepareStatement(
                        "SELECT * FROM members WHERE Member_ID = ?");

                checkMember.setInt(1, memberid);

                ResultSet memberRs = checkMember.executeQuery();

                if (memberRs.next()) {

                    System.out.println("Member ID already exists");

                    memberRs.close();
                    checkMember.close();
                    break;
                }

                memberRs.close();
                checkMember.close();

                System.out.println("Enter your name");
                mname = sc.nextLine();

                System.out.println("Enter phone number");
                phone = sc.nextLine();

                System.out.println("Enter email address");
                mail = sc.nextLine();

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO members(Member_ID,Name,Phone,Email) VALUES(?,?,?,?)");

                ps.setInt(1, memberid);
                ps.setString(2, mname);
                ps.setString(3, phone);
                ps.setString(4, mail);

                ps.executeUpdate();

                System.out.println("Member registered successfully");

                ps.close();

                break;

            case 2:

                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("SELECT * FROM books");

                System.out.println("\nAvailable Books");

                while (rs.next()) {

                    System.out.println(
                            "\nBook ID  : " + rs.getString("Book_ID") +
                            "\nTitle    : " + rs.getString("Title") +
                            "\nAuthor   : " + rs.getString("Author") +
                            "\nGenre    : " + rs.getString("Genre") +
                            "\nStatus   : " + rs.getString("Status") +
                            "\nQuantity : " + rs.getInt("Quantity"));
                }

                rs.close();
                st.close();

                break;

            case 3:

                sc.nextLine();

                System.out.println("Issue books option selected");

                System.out.println("Enter book name");
                bookname = sc.nextLine();

                PreparedStatement bookCheck =
                        con.prepareStatement(
                        "SELECT * FROM books WHERE Title = ?");

                bookCheck.setString(1, bookname);

                ResultSet bookRs = bookCheck.executeQuery();

                if (bookRs.next()) {

                    if (bookRs.getInt("Quantity") > 0) {

                        System.out.println("Book issued");

                    } else {

                        System.out.println("Book currently unavailable");
                    }

                } else {

                    System.out.println("Book does not exist");
                }

                bookRs.close();
                bookCheck.close();

                break;

            case 4:

                sc.nextLine();

                System.out.println("Is the book returned after due date? (yes/no)");

                String answer = sc.nextLine();

                if (answer.equalsIgnoreCase("yes")) {

                    System.out.println("Enter number of late days");

                    day = sc.nextInt();

                    fine = day * 20;

                    System.out.println("Pay fine of Rs " + fine);

                } else {

                    System.out.println("Book returned on time");
                    System.out.println("No fine");
                }

                break;

            default:

                System.out.println("Invalid choice");
            }

            con.close();

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}