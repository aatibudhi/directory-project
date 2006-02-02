package org.apache.directory.daemon.installers;


import java.io.File;
import java.util.Calendar;
import java.util.List;


public class Application
{
    private String name;
    private String description;
    private String company;
    private String email;
    private String url;
    private String version;
    private String minimumJavaVersion;
    private String copyrightYear;
    private List authors;
    private File icon;
    private File readme;
    private File license;
    private String licenseType = "ASL 2.0" ;
    

    public Application()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( System.currentTimeMillis() );
        setCopyrightYear( String.valueOf( cal.get( Calendar.YEAR ) ) );
    }


    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }


    public String getCopyrightYear()
    {
        return copyrightYear;
    }


    public void setLicense(File license)
    {
        this.license = license;
    }


    public File getLicense()
    {
        return license;
    }


    public void setReadme(File readme)
    {
        this.readme = readme;
    }


    public File getReadme()
    {
        return readme;
    }


    public void setIcon(File icon)
    {
        this.icon = icon;
    }


    public File getIcon()
    {
        return icon;
    }


    public void setAuthors(List authors)
    {
        this.authors = authors;
    }


    public List getAuthors()
    {
        return authors;
    }


    public void setMinimumJavaVersion(String minimumJavaVersion)
    {
        this.minimumJavaVersion = minimumJavaVersion;
    }


    public String getMinimumJavaVersion()
    {
        return minimumJavaVersion;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return version;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return url;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return email;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public String getCompany()
    {
        return company;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }


    public String getLicenseType()
    {
        return licenseType;
    }
}
