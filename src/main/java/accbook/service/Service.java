package accbook.service;

import accbook.dao.DAO;
import accbook.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class Service
{
    @Autowired
    private DAO dao;

    public List<ExpKindsList> expKindsList() throws SQLException
    {
        return dao.expKinsList();
    }

    public List<AccExpList> accExpList(String loginID, String regDate) throws SQLException
    {
        return dao.accExpList(loginID, regDate);
    }

    public int addExpData(AddExpData aed) throws SQLException
    {
        //못된놈 쫓아내기
        if(aed.getDate().length()>10 && aed.getKinds() > 10 && (aed.getMoney().length() > String.valueOf(Integer.MAX_VALUE).length() && aed.getUseRea().length() > 30))
        {
            return 10;
        }

        if(aed.getDate().length()>10)
        {
            return 2;
        }

        if(aed.getKinds() > 10)
        {
            return 3;
        }

        if(aed.getMoney().length() > String.valueOf(Integer.MAX_VALUE).length())
        {
            return 4;
        }

        if(aed.getUseRea().length() > 30)
        {
            return 5;
        }

        return dao.addExpData(aed);
    }

    public int deldExpData(String[] expSeq) throws SQLException
    {
        return dao.deldExpData(expSeq);
    }

    public int updateExpData(UpdateExpData ued) throws SQLException
    {
        ued.setMoney(ued.getMoney().replaceAll(",", ""));
        return dao.updatedExpData(ued);
    }

    public List<TotalList> totalCount(TotalCount tc) throws SQLException
    {
        return dao.totalCount(tc);
    }

    public int loginCheck(LoginVO lv) throws SQLException
    {
        return dao.loginCheck(lv);
    }

}
