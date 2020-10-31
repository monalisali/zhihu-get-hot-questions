package Dto;

public class QuestionResultDto {
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    private int pageIndex;

    public String getPagedHtmlResponse() {
        return pagedHtmlResponse;
    }

    public void setPagedHtmlResponse(String pagedHtmlResponse) {
        this.pagedHtmlResponse = pagedHtmlResponse;
    }

    private String pagedHtmlResponse;
}
