import com.google.protobuf.Api;
import data.APIHelper;
import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BackendTest {


    @Test
    public void validTransferFromFirstToSecond(){
        var authInfo = DataHelper.getAuthInfoWithTestData();
        APIHelper.sendLoginRequest(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationUser(authInfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendVerificationCodeRequest(verificationInfo,200);
        var cardsBalances = APIHelper.sendRequestToGetCardBalances(tokenInfo.getToken(),200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFirstCard().getId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCard().getId());
        var amount = DataHelper.generateValidAmount(firstCardBalance);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getFirstCard().getNumber(),
                DataHelper.getSecondCard().getNumber(), amount);
        APIHelper.sendRequestToTransferAmount(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendRequestToGetCardBalances(tokenInfo.getToken(),200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFirstCard().getId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCard().getId());
        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));

    }
}
