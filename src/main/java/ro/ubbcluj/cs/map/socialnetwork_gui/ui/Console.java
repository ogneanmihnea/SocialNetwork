package ro.ubbcluj.cs.map.socialnetwork_gui.ui;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Console implements ConsoleInterface {

    private Service serv;

    public Console(Service<Long> serv) {
        this.serv = serv;
    }

    public void AdaugaUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Numele este: ");
        String nume = scanner.nextLine();

        System.out.print("Prenumele este: ");
        String prenume = scanner.nextLine();

        try {
            Utilizator user = new Utilizator(nume, prenume,null,null);
            if (serv.addUser(user)) {
                System.out.println("Utilizator adaugata cu succes!");
            } else {
                System.out.println("Nu s-a reusit adaugarea de utilizator!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void StergeUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("ID-ul de sters: ");
        Long id = scanner.nextLong();
        try {
            if (!serv.deleteUser(id)) {
                System.out.println("S-a sters utilizatorul!");
            } else {
                System.out.println("Nu s-a reusit stergerea utilizatorului!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Nu s-a realizat bine stergerea!");
        }
    }

    public void PrintAllUsers() {
        if (!serv.getAllUsers().iterator().hasNext()) {
            System.out.println("Nu exista utilizatori!");
        } else {
            for (var u : serv.getAllUsers()) {
                System.out.println(u);
            }
        }
    }

    public void AdaugaPr() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Id-ul primului utilizator este: ");
        var id1 = scanner.nextLong();

        System.out.print("Id-ul la al doilea utilizator este: ");
        var id2 = scanner.nextLong();

        try {
            if (serv.addFriendship(id1, id2)) {
                System.out.println("Prietenie adaugata cu succes!");
            } else {
                System.out.println("Nu s-a reusit adaugarea de prietenie!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void StergePr() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Id-ul primului utilizator este: ");
        var id1 = scanner.nextLong();

        System.out.print("Id-ul la al doilea utilizator este: ");
        var id2 = scanner.nextLong();

        try {
            if (!serv.deleteFriendship(id1, id2)) {
                System.out.println("Prietenie stearsa cu succes!");
            } else {
                if (!serv.deleteFriendship(id2, id1)) {
                    System.out.println("Prietenie stearsa cu succes!");
                } else {
                    System.out.println("Nu s-a reusit stergerea prieteniei!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void PrintAllPr() {
        if (!serv.getAllFriendships().iterator().hasNext()) {
            System.out.println("Nu exista prietenii!");
        } else {
            for (var u : serv.getAllFriendships()) {
                System.out.println(u);
            }
        }
    }

    public void MyFriends() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Id-ul utilizatorului: ");
        var id1 = scanner.nextLong();

        try {
            if (serv.getPrUtilizatorului(id1) == null) {
                System.out.println("Utlizatorul nu are prieteni!");
            } else {
                for (var ut : serv.getPrUtilizatorului(id1)) {
                    System.out.println(ut);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void PrintNrCom() {
        int nr = serv.Comunitati();
        if (nr == 0) {
            System.out.println("Nu exista comunitati!");
        } else {
            System.out.printf("Numarul de comunitati este: %d\n", nr);
        }
    }

    public void PrintSociabila() {
        List lst = serv.Sociabila();
        if (lst.isEmpty()) {
            System.out.println("Nu exista comunitati!");
        } else {
            for (var ut : lst) {
                System.out.println(ut);
            }
        }
    }

    public void PrintPrLuna() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Id-ul utilizatorului: ");
        var id1 = scanner.nextLong();
        System.out.println("Luna dorita: ");
        var month = scanner.next();
        List<Prietenie> all = serv.ShowPrLuna(id1, month);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if (all.isEmpty()) {
                System.out.println("Utlizatorul nu are prieteni in luna respectiva!");
            } else {
                System.out.println("Prietenii utilizatorului cu id-ul " + id1 + " sunt:");
                all.forEach(p -> {
                    if (p.getUser1().getId().equals(id1)) {
                        System.out.println(p.getUser2().getFirstName() + " | "
                                + p.getUser2().getLastName() + " | " + formatter.format(p.getDate()));
                    } else {
                        System.out.println(p.getUser1().getFirstName() + " | "
                                + p.getUser1().getLastName() + " | " + formatter.format(p.getDate()));
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute() {

        while (true) {
            System.out.println("Meniu:");
            System.out.print("adaugauser ");
            System.out.print("stergeuser ");
            System.out.print("allusers ");
            System.out.print("adaugapr ");
            System.out.print("stergepr ");
            System.out.print("allpr ");
            System.out.print("myfriends ");
            System.out.print("nrcomunitati ");
            System.out.print("sociabila ");
            System.out.print("showprluna ");
            System.out.print("exit");
            System.out.print("\n Comanda este: ");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "adaugauser":
                    AdaugaUser();
                    break;
                case "stergeuser":
                    StergeUser();
                    break;
                case "allusers":
                    PrintAllUsers();
                    break;
                case "adaugapr":
                    AdaugaPr();
                    break;
                case "stergepr":
                    StergePr();
                    break;
                case "allpr":
                    PrintAllPr();
                    break;
                case "myfriends":
                    MyFriends();
                    break;
                case "nrcomunitati":
                    PrintNrCom();
                    break;
                case "sociabila":
                    PrintSociabila();
                    break;
                case "showprluna":
                    PrintPrLuna();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Nu ai ales bine optiunea!");
            }
        }
    }
}
