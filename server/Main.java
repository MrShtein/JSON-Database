package server;

public class Main {

    public static void main(String[] args) {
        Db db = new Db();

        boolean work = true;
        DataGrabber dg = new DataGrabber();
        while (dg.handleData()) {
            try {
                switch (dg.getMethod()) {
                    case "get":
                        System.out.println(db.getValue(Integer.parseInt(dg.getNum())));
                        break;
                    case "set":
                        db.setValue(Integer.parseInt(dg.getNum()), dg.getValue());
                        break;
                    case "delete":
                        db.deleteValue(Integer.parseInt(dg.getNum()));
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
