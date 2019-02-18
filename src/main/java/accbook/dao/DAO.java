package accbook.dao;

import accbook.vo.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAO
{
    private Connection getConnection(Connection con)
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
            con = DriverManager.getConnection(dbUrl, "IM10", "nets1234");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }

    private void getClose(ResultSet rs, PreparedStatement pstmt, Connection con) throws SQLException
    {
        if (rs != null)
            rs.close();
        if (pstmt != null)
            pstmt.close();
        if (con != null)
            con.close();
    }

    public List<ExpKindsList> expKinsList() throws SQLException
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        List<ExpKindsList> list = new ArrayList<>();
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement("SELECT arti_seq, arti_kinds FROM arti_kinds");
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                ExpKindsList ekl = new ExpKindsList();
                ekl.setExpSeq(rs.getInt("arti_seq"));
                ekl.setExpKinds(rs.getString("arti_kinds"));
                list.add(ekl);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            getClose(rs, pstmt, con);
        }
        return list;
    }

    public List<AccExpList> accExpList(String loginID, String regDate) throws SQLException
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        List<AccExpList> list = new ArrayList<>();
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement("SELECT a.exp_seq, b.arti_kinds, to_char(a.arti_money, '999,999,999,999') arti_money, a.user_rea, TO_CHAR(a.reg_date,'YYYY-MM-DD') reg_date, to_char(a.update_date, 'YYYY-MM-DD') update_date FROM acc_exp a JOIN arti_kinds b ON a.arti_seq = b.arti_seq WHERE a.login_id = ? AND reg_date = ?");
            pstmt.setString(1, loginID);
            pstmt.setString(2, regDate);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                AccExpList ael = new AccExpList();
                ael.setExpSeq(rs.getInt("exp_seq"));
                ael.setArtiKinds(rs.getString("arti_kinds"));
                ael.setArtiMoney(rs.getString("arti_money"));
                ael.setUserRea(rs.getString("user_rea"));
                ael.setRegDate(rs.getString("reg_date"));
                ael.setUpdateDate(rs.getString("update_date"));
                list.add(ael);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            getClose(rs, pstmt, con);
        }
        return list;
    }

    public int addExpData(AddExpData aed) throws SQLException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement("INSERT INTO acc_exp (exp_seq, login_id, arti_seq, arti_money, user_rea, reg_date) VALUES ((SELECT nvl(MAX(exp_seq),0)+1 AS exp_seq FROM acc_exp), ?, ?, ?, ?, ?) ");
            pstmt.setString(1, aed.getLoginID());
            pstmt.setInt(2, aed.getKinds());
            pstmt.setString(3, aed.getMoney());
            pstmt.setString(4, aed.getUseRea());
            pstmt.setString(5, aed.getDate());
            return pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        }
        return 0;
    }

    public int deldExpData(String[] expSeq) throws SQLException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try
        {
            con = getConnection(con);
            int sum = 0;
            for (int i = 0; i < expSeq.length; i++)
            {
                pstmt = con.prepareStatement("DELETE acc_exp WHERE exp_seq = ?");
                pstmt.setString(1, expSeq[i]);
                sum++;
                pstmt.executeUpdate();
            }
            return sum;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        }
        return 0;
    }

    public int updatedExpData(UpdateExpData ued) throws SQLException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement("UPDATE acc_exp SET arti_seq = (SELECT arti_seq FROM arti_kinds WHERE arti_kinds = ?), arti_money = ?, user_rea = ?, reg_date = ?  WHERE exp_seq = ?");
            pstmt.setString(1, ued.getKinds());
            pstmt.setString(2, ued.getMoney());
            pstmt.setString(3, ued.getUseRea());
            pstmt.setString(4, ued.getDate());
            pstmt.setInt(5, ued.getExpSeq());
            return pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        }
        return 0;
    }

    public List<TotalList> totalCount(TotalCount tc) throws SQLException
    {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        List<TotalList> list = new ArrayList<>();
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement(" SELECT a.arti_kinds, NVL(b.arti_money, 0) arti_money from arti_kinds a LEFT JOIN ( SELECT arti_seq, to_char(sum(arti_money), '999,999,999,999') arti_money FROM acc_exp WHERE login_id = ? AND reg_date between ? AND ?  group by arti_seq) b on a.arti_seq=b.arti_seq");
            pstmt.setString(1, tc.getLoginID());
            pstmt.setString(2, tc.getStartDate());
            pstmt.setString(3, tc.getEndDate());
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                TotalList tl = new TotalList();
                tl.setArtiKinds(rs.getString("arti_kinds"));
                tl.setArtiMoney(rs.getString("arti_money"));
                list.add(tl);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            getClose(rs, pstmt, con);
        }
        return list;
    }

    public int loginCheck(LoginVO lv) throws SQLException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            con = getConnection(con);
            pstmt = con.prepareStatement("SELECT COUNT(*) cnt FROM acc_user WHERE login_id = ? AND pwd = ?");
            pstmt.setString(1, lv.getId());
            pstmt.setString(2, lv.getPwd());
            rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("cnt");
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            getClose(rs, pstmt, con);
        }
        return 0;
    }
}
