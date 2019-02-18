package accbook.controller;

import accbook.service.Service;
import accbook.vo.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@SessionAttributes({"id"})
public class Main
{

    @Autowired
    private Service s;

    private void sessionCheck(HttpSession session, HttpServletResponse response, HttpServletRequest request)
    {
        if (session.getAttribute("id") == null)
        {
            try
            {
                PrintWriter writer = null;
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8;");
                request.setCharacterEncoding("UTF-8");
                writer = response.getWriter();
                writer.println("<script>alert('세션이 만료되었습니다. 로그인 페이지로 이동합니다.'); location.replace('../index.do') </script>");
                writer.flush();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/index", method = {POST, GET})
    public String index()
    {
        return "login";
    }

    /*로그인 아이디 패스워드 받아서 ajax에 1인지 리턴하는거*/
    @RequestMapping(value = "login", method = {POST, GET})
    @ResponseBody
    public int login(Model model, LoginVO lv) throws SQLException
    {

        model.addAttribute("id", lv.getId());
        return s.loginCheck(lv);
    }


    @RequestMapping(value = "{loginID}/main", method = {GET, POST})
    public String main(@PathVariable("loginID") String loginID, HttpSession session, Model model, String regDate, String startDate, String endDate, HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        sessionCheck(session, response, request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (regDate == null)
        {
            regDate = sdf.format(new Date());
        }

        String id = (String) session.getAttribute("id");
        model.addAttribute("expKindsList", s.expKindsList());
        model.addAttribute("accExpList", s.accExpList(id, regDate));
        model.addAttribute("regDate", regDate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "main";
    }

    @GetMapping(value = "logout")
    public String logout()
    {
        return "index";
    }

    @RequestMapping(value = "{loginID}/expAdd", method = {POST, GET})
    @ResponseBody
    public int expAdd(@PathVariable("loginID") String loginID, AddExpData aed, HttpSession session) throws SQLException
    {
        aed.setLoginID((String) session.getAttribute("id"));
        return s.addExpData(aed);
    }

    @RequestMapping(value = "{loginID}/regDate", method = {GET, POST})
    @ResponseBody
    public List<AccExpList> regDate(@PathVariable("loginID") String loginID, String regDate, HttpSession session, Model model) throws SQLException
    {
        String id = (String) session.getAttribute("id");
        List<AccExpList> list = s.accExpList(id, regDate);

        return list;
    }

    @RequestMapping(value = "{loginID}/deleteExp", method = {POST})
    @ResponseBody
    public JSONObject deleteExp(@PathVariable("loginID") String loginID, String[] expSeqArr, String regDate, HttpSession session, Model model) throws SQLException
    {
        JSONObject json = new JSONObject();
        json.put("cnt", s.deldExpData(expSeqArr));
        json.put("regDate", regDate);

        return json;
    }

    @RequestMapping(value = "{loginID}/update", method = {POST})
    @ResponseBody
    public int updateExp(@PathVariable("loginID") String loginID, UpdateExpData ued) throws SQLException
    {
        return s.updateExpData(ued);
    }

    @RequestMapping(value = "{loginID}/totalCnt", method = {GET, POST})
    @ResponseBody
    public JSONObject totalCnt(@PathVariable("loginID") String loginID, TotalCount tc, Model model, HttpSession session) throws SQLException
    {
        if (tc.getStartDate() == null && tc.getEndDate() == null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tc.setStartDate(sdf.format(new Date()));
            tc.setEndDate(sdf.format(new Date()));
        }
        tc.setLoginID((String) session.getAttribute("id"));

        List<TotalList> list = s.totalCount(tc);
        JSONObject json = new JSONObject();
        for (int i = 0; i < list.size(); i++)
        {
            json.put(list.get(i).getArtiKinds(), list.get(i).getArtiMoney());
        }

        return json;
    }

    @RequestMapping(value = "{loginID}/totalPage")
    public String totalPage(@PathVariable("loginID") String loginID, TotalCount tc, Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        sessionCheck(session, response, request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        tc.setLoginID((String) session.getAttribute("id"));
//        tc.setStartDate(sdf.format(new Date()));
//        tc.setEndDate(sdf.format(new Date()));
        model.addAttribute("tcList", s.totalCount(tc));
        return "totalPage";
    }

}
