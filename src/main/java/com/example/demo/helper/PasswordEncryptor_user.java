//package com.example.demo.helper;
//
//import java.util.List;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.mindrot.jbcrypt.BCrypt;
//
//import com.example.demo.model.UserTableBean;
//
//public class PasswordEncryptor_user {
//    public static void main(String[] args) {
//        Transaction transaction = null;
//
// 
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            transaction = session.beginTransaction();
//
//            List<UserTableBean> users = session.createQuery("FROM UserBean", UserTableBean.class).getResultList();
//            for (UserTableBean user : users) {
//                String plainPassword = user.getPassword();
//                if (!plainPassword.startsWith("$2a$")) { 
//                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
//                    user.setPassword(hashedPassword);
//                    session.update(user);
//                }
//            }
//            transaction.commit();
//            System.out.println("所有密碼已成功加密並更新。");
//        } catch (Exception e) {
//            if (transaction != null) {
//            	System.out.println("failed change");
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }
//}
