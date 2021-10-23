// This class will take in urls that have commas or values that are not valid when using as filenames
// this will be implemented in the future, for now we will just ignore anything that has a comma

public class UrlFormatter {
    String rawUrl = "";
    String formattedUrl = "";

    public UrlFormatter(){

    }

    public UrlFormatter(String rawUrl){
        this.rawUrl = rawUrl;
    }

    public String format(){
        String noHead = rawUrl.replace("://", "<headslash>");
        String noSlashes = noHead.replaceAll("/","<slash>");
        return noSlashes;

    }

    public String format(String rawUrl){
        String noHead = rawUrl.replace("://", "<headslash>");
        String noSlashes = noHead.replaceAll("/","<slash>");
        return noSlashes;

    }

    public String revert(String seedUrl){
        String addHead = rawUrl.replace("<headslash>","://");
        String addSlashes = addHead.replaceAll("<slash>","/");
        return addSlashes;
    }
}
