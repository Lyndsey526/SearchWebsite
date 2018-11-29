import java.util.*;

public class KeyCounts {
    private Sites site;
    private int count;

    public KeyCounts(Sites s, int cnt) {
        site = s;
        count = cnt;
    }

    public int getCount()
    {
        return count;
    }

    public Sites getSite()
    {
        return site;
    }
}

class KeyCountComparer implements Comparator<KeyCounts>
{
    @Override
    public int compare(KeyCounts c1, KeyCounts c2)
    {
        return c2.getCount() - c1.getCount();
    }
}
//stores key counts by array list and then sorts it