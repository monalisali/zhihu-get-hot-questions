package Dto;

public class JingDongRequestDataDto {
    private String keywordType;
    private String searchType;
    private String key;
    private int categoryId;
    private int cat2Id;
    private int cat3Id;

    public JingDongRequestDataDto(String keywordType, String searchType,String key,int categoryId) {
        this.keywordType = keywordType;
        this.searchType = searchType;
        this.key = key;
        this.categoryId = categoryId;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public String getKey() {
        return key;
    }

    public int getCategoryId() { return categoryId; }



}
