package modules;

import Dto.QuestionResultDto;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.List;


public interface Question {
    List<QuestionResultDto> getQuestion();
}
