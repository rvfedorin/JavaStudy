import java.util.ArrayList;

class test
{
    public static void main(String [] args) {
        final String abc = "abcdefg";
        final String numeric = "1234567";
        String used = "";
        int numPosition = 3;

        boolean searching = true;
        int tooMuch = 0;
        String cell = "";
        ArrayList<String> allPosition = new ArrayList<>();
        int startPosition = (int) (Math.random() * 6);

        int x = startPosition;
        int y = startPosition;


        while (searching && tooMuch++ < 200 && numPosition > 0)
        {
            int direction = (int) (Math.random() * 3);
//            System.out.println("Направление: " + direction);
//            System.out.println("startPosition: " + startPosition);

            switch (direction)
            {
                case 0:
                    if (x > 0 && y < 7)
                    {
                        String hor = numeric.substring(x-1, x);
                        String vert = abc.substring(y, y+1);
                        cell = vert + hor;
                        if (used.contains(cell))  // Если такое полу уже есть, пробуем заново
                        {continue;}

                        used += cell;
                        numPosition--;
                        x -= 1;
                    }
                    break;
                case 1:
                    if (y > 0 && x < 7)
                    {
                        String hor = numeric.substring(x, x+1);
                        String vert = abc.substring(y-1, y);
                        cell = vert + hor;
                        if (used.contains(cell))  // Если такое полу уже есть, пробуем заново
                        {continue;}
                        used += cell;
                        numPosition--;
                        y -= 1;
                    }
                    break;
                case 2:
                    if (x < 6 && y < 6)
                    {
                        String hor = numeric.substring(x, x+1);
                        String vert = abc.substring(y, y+1);
                        cell = vert + hor;
                        if (used.contains(cell))  // Если такое полу уже есть, пробуем заново
                        {continue;}
                        used += cell;
                        numPosition--;
                        x += 1;
                    }
                    break;
                case 3:
                    if (x < 6 && y < 6)
                    {
                        String hor = numeric.substring(x, x+1);
                        String vert = abc.substring(y, y+1);
                        cell = vert + hor;
                        if (used.contains(cell))  // Если такое полу уже есть, пробуем заново
                        {continue;}
                        used += cell;
                        numPosition--;
                        y += 1;
                    }
                    break;

            }
        }
        System.out.println(used);
//        System.out.println(abc.substring(5,6));

    }

}
