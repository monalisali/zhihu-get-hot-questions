package Dto;

import java.util.Objects;

public class QuestionResultDto {
    private int pageIndex;
    private String pagedHtmlResponse;
    private int linkIndex;
    private String link;
    private String deCodeLink;


    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPagedHtmlResponse() {
        return pagedHtmlResponse;
    }

    public void setPagedHtmlResponse(String pagedHtmlResponse) {
        this.pagedHtmlResponse = pagedHtmlResponse;
    }


    public int getLinkIndex() {
        return linkIndex;
    }

    public void setLinkIndex(int linkIndex) {
        this.linkIndex = linkIndex;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeCodeLink() {
        return deCodeLink;
    }

    public void setDeCodeLink(String deCodeLink) {
        this.deCodeLink = deCodeLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionResultDto that = (QuestionResultDto) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
