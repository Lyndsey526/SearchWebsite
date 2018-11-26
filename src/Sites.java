public class Sites
{
    private int link_id;
    private String site_name, url, source_code;

    public Sites(int lid, String sname, String url, String code)
    {
        link_id = lid;
        site_name = sname;
        this.url = url;
        source_code = code;
    }

    public int getlinkid()
    {
        return link_id;
    }

    public String getsitename()
    {
        return site_name;
    }

    public String geturl()
    {
        return url;
    }

    public String getsourcecode()
    {
        return source_code;
    }
}
