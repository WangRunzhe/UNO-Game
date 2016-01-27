package game;

import Entity.Player;
import java.io.IOException;
import java.util.Optional;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Summer
 */
@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {

    @EJB
    private PlayerManager pm;

    @Inject
    private RunningGame rgame;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Comes to login servlet!!!");
        String path = req.getPathInfo();
        System.out.println("The Path is: " + path);
        HttpSession session = req.getSession();
        switch (path) {

            case "/signup":
                String playerid = req.getParameter("playeridtxt");
                String password = req.getParameter("passwordtxt");
                System.out.println("player Info: >>>>>" + playerid + "   " + password);

                Player p = pm.getPlayer(playerid);
                System.out.println("p----"+ p);

                if (null == p) {
                    Player player = new Player();
                    player.setId(playerid);
                    player.setPassword(password);
                    pm.add(player);
                    //rgame.addregisteredplayer(p);
                    //resp.sendRedirect("createorjoin.html#" + playerid );
                    session.setAttribute("id", playerid);
                    req.getRequestDispatcher("/createorjoin.jsp").forward(req, resp);
                } else {
                    session.setAttribute("status", playerid+ "Already Existing");
                    req.getRequestDispatcher("/signup.jsp").forward(req, resp);
                }

                break;

            case "/signin":
                String loginid = req.getParameter("loginid");
                String loginpassword = req.getParameter("loginpaw");

                System.out.println("Login Id=" + loginid);
                System.out.println("Login Password=" + loginpassword);

                Optional<Player> opt = pm.getPlayer(loginid, loginpassword);

                session.setAttribute("id", loginid);

                if (opt.isPresent()) {
                    session.setAttribute("status", "Successfully Login!");
                    req.getRequestDispatcher("/createorjoin.jsp").forward(req, resp);
                    // resp.sendRedirect("createorjoin.html#" + loginid);
                } else {
                    session.setAttribute("status", "Wrong Password or ID.");
                    req.getRequestDispatcher("/signin.jsp").forward(req, resp);
                    // resp.sendRedirect("signin.html#" + loginid);
                }
                break;

        }

    }

}
