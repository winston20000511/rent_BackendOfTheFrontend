package util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import Bean.AdminBean;
import Bean.UserBean;

public class PasswordEncryptor_user {
    public static void main(String[] args) {
        Transaction transaction = null;

 
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<UserBean> users = session.createQuery("FROM UserBean", UserBean.class).getResultList();
            for (UserBean user : users) {
                String plainPassword = user.getPassword();
                if (!plainPassword.startsWith("$2a$")) { 
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    user.setPassword(hashedPassword);
                    session.update(user);
                }
            }
            transaction.commit();
            System.out.println("所有密碼已成功加密並更新。");
        } catch (Exception e) {
            if (transaction != null) {
            	System.out.println("failed change");
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
