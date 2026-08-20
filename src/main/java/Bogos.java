import java.util.Scanner;

public class Bogos {
    public static void main(String[] args) {
        String horizString = "____________________________________________________________";
        String indentString = "         ";
        String bannerString = """
      ___             __ _                  
     | _ )    ___    / _` |   ___     ___   
     | _ \\   / _ \\   \\__, |  / _ \\   (_-<   
     |___/   \\___/   |___/   \\___/   /__/_  
   _|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| 
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Hey. I'm Bogos.
Can I help you get your photos printed? (How can I help you?)""";
        String byeString = "Bogos binted. (Bye!)";
        System.out.println(bannerString);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            System.out.println(horizString);

            if (command.equals("bye")) {
                System.out.println(indentString + byeString);
                System.out.println(horizString);
                break;
            }

            System.out.println(indentString + command);
            System.out.println(horizString);
        }
    }
}
