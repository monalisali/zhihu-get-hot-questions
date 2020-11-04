package Dto;

public class JingDongRequestDataDto {
    private String keywordType;
    private String searchType;
    private String key;

    public JingDongRequestDataDto(String keywordType, String searchType,String key) {
        this.keywordType = keywordType;
        this.searchType = searchType;
        this.key = key;
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


}
