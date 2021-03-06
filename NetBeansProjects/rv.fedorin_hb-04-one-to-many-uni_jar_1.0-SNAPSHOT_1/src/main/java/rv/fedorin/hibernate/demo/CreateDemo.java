/*
 * Proprietary software.
 * 
 */
package rv.fedorin.hibernate.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import rv.fedorin.hibernate.demo.entity.Instructor;
import rv.fedorin.hibernate.demo.entity.InstructorDetail;

/**
 *
 * @author R. V. Fedorin
 */
public class CreateDemo {

    public static void main(String[] args) {

        try ( SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .buildSessionFactory()) {

            // create ssession
            Session session = factory.getCurrentSession();

            // start a transaction
            session.beginTransaction();
            
            Instructor instructor = new Instructor("Madhu", "Patel", "patel@email.com");
            InstructorDetail instructorDetail = 
                    new InstructorDetail("youtube.com/patel", "Guitar");
            
            instructor.setInstructorDetail(instructorDetail);
            
            System.out.println("[*] Saving instructor: " + instructor);
            session.save(instructor);
            
            // commit transaction
            session.getTransaction().commit();
            
            System.out.println("[*] Done");
        }
    } // ** main()
} // ** class CreateStudentDemo
