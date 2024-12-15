package org.practica;

import org.hibernate.Session;
import org.practica.entities.Ticket;
import org.practica.entities.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    private static Session session = null;

    public static void main(String[] args) {

        try {
            session = HibernateUtil.getSession();
//                crearUsuarios();
//                crearTickets();
//                leerTablas();
//                crearRelaciones();

            //Operaciones con tablas
//            actualizarTickets(3, "Tunel del terror");
//            eliminarUsuarios(4);

            //Consultas HQL

            //Consultar tickets de un usuario
            ticketUsuario(2);
            System.out.println("-------------------------------------------------");
            //Entradas de una atraccion en concreto
            entradasAtraccion("Noria");
            System.out.println("-------------------------------------------------");
            //Gasto medio de un usuario en las atracciones
            gastoMedioUsuario(1);
            System.out.println("-------------------------------------------------");
            //Gasto total de un usuario
            gastoTotalUsuario(2);
            System.out.println("-------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.closeSession(session);
        }
    }

    private static void gastoTotalUsuario(int id) {
        String hql = "select sum(precio) from Ticket where usuario.id = :id";

        BigDecimal gasto = session.createQuery(hql, BigDecimal.class)
                .setParameter("id", id)
                .uniqueResult();

        if (gasto != null) {
            System.out.println("Gasto total del usuario con id " + id + ": " + gasto + "€");
        } else {
            System.out.println("No se encontraron tickets para el usuario con id " + id);
        }
    }

    private static void gastoMedioUsuario(int id) {
        String hql = "select avg(precio) from Ticket where usuario.id = :id";

        Double gasto = session.createQuery(hql, Double.class)
                .setParameter("id", id)
                .uniqueResult();

        if (gasto != null) {
            System.out.println("Gasto medio del usuario con id " + id + ": " + gasto + "€");
        } else {
            System.out.println("No se encontraron tickets para el usuario con id " + id);
        }
    }

    private static void entradasAtraccion(String atraccion) {
        String hql = "select count(*) from Ticket where nombreAtraccion = :atraccion";

        Long tickets = session.createQuery(hql, Long.class)
                .setParameter("atraccion", atraccion)
                .uniqueResult();

        if (tickets != null) {
            System.out.println("Tickets de la atracción " + atraccion + ": " + tickets);
        } else {
            System.out.println("No se encontraron tickets para la atracción " + atraccion);
        }
    }

    private static void ticketUsuario(int id) {
        String hql = "from Ticket where usuario.id = :id";

        List<Ticket> tickets = session.createQuery(hql, Ticket.class)
                .setParameter("id", id)
                .list();

        if (!tickets.isEmpty()) {
            System.out.println("Tickets del usuario con id " + id + ":");
            for (Ticket ticket : tickets) {
                System.out.println("Ticket: " + ticket.getNombreAtraccion());
            }
        } else {
            System.out.println("No se encontraron tickets para el usuario con id " + id);
        }
    }

    private static void eliminarUsuarios(int i) {
        Usuario usuario = session.get(Usuario.class, i);
        if (usuario != null) {
            session.beginTransaction();
            session.remove(usuario);
            session.getTransaction().commit();

            System.out.println("Usuario " + i + " eliminado de la base de datos");
        } else {
            System.out.println("Id no encontrado");
        }
    }

    private static void actualizarTickets(int id, String atraccion) {
        Ticket ticket = session.get(Ticket.class, id);

        if (ticket != null) {
            session.beginTransaction();
            ticket.setNombreAtraccion(atraccion);
            session.merge(ticket);
            session.getTransaction().commit();

            System.out.println("Ticket actualizado correctamente a " + atraccion);
        } else {
            System.out.println("Id no encontrado");
        }

    }

    private static void crearRelaciones() {
        //En esta función creamos nuevas entradas relacionando tickets con usuarios
        session.beginTransaction();
        Usuario usuario1 = session.get(Usuario.class, 1);
        Ticket ticket1 = new Ticket();
        ticket1.setNombreAtraccion("Montaña Rusa");
        ticket1.setPrecio(BigDecimal.valueOf(4.50));
        ticket1.setUsuario(usuario1);
        session.persist(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setNombreAtraccion("Noria");
        ticket2.setPrecio(BigDecimal.valueOf(1.50));
        ticket2.setUsuario(usuario1);
        session.persist(ticket2);

        Usuario usuario2 = session.get(Usuario.class, 2);
        Ticket ticket3 = new Ticket();
        ticket3.setNombreAtraccion("Tren de la bruja");
        ticket3.setPrecio(BigDecimal.valueOf(3.50));
        ticket3.setUsuario(usuario2);
        session.persist(ticket3);
        Ticket ticket4 = new Ticket();
        ticket4.setNombreAtraccion("Noria");
        ticket4.setPrecio(BigDecimal.valueOf(1.50));
        ticket4.setUsuario(usuario2);
        session.persist(ticket4);

        Usuario usuario3 = session.get(Usuario.class, 3);
        Ticket ticket5 = new Ticket();
        ticket5.setNombreAtraccion("Montaña Rusa");
        ticket5.setPrecio(BigDecimal.valueOf(4.50));
        ticket5.setUsuario(usuario3);
        session.persist(ticket5);

        Usuario usuario4 = session.get(Usuario.class, 4);
        Ticket ticket6 = new Ticket();
        ticket6.setNombreAtraccion("Tren de la bruja");
        ticket6.setPrecio(BigDecimal.valueOf(3.50));
        ticket6.setUsuario(usuario4);
        session.persist(ticket6);


        session.getTransaction().commit();
    }

    private static void leerTablas() {
        List<Usuario> usuarios = session.createQuery("from Usuario", Usuario.class).list();
        for (Usuario usuario : usuarios) {
            System.out.println("Usuario: " + usuario.getNombre() + " con id: " + usuario.getId());
        }

        List<Ticket> tickets = session.createQuery("from Ticket", Ticket.class).list();
        for (Ticket ticket : tickets) {
            System.out.println("Id: " + ticket.getId() + " Nombre: " + ticket.getNombreAtraccion() + " Precio: " + ticket.getPrecio());
        }
    }

    private static void crearTickets() {
        session.beginTransaction();
        Ticket ticket1 = new Ticket();
        ticket1.setNombreAtraccion("Montaña Rusa");
        ticket1.setPrecio(BigDecimal.valueOf(4.50));
        session.persist(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setNombreAtraccion("Noria");
        ticket2.setPrecio(BigDecimal.valueOf(1.50));
        session.persist(ticket2);

        Ticket ticket3 = new Ticket();
        ticket3.setNombreAtraccion("Tren de la bruja");
        ticket3.setPrecio(BigDecimal.valueOf(3.50));
        session.persist(ticket3);

        session.getTransaction().commit();

    }

    private static void crearUsuarios() {
        session.beginTransaction();
        Usuario user1 = new Usuario();
        user1.setNombre("Juan");
        session.persist(user1);

        Usuario user2 = new Usuario();
        user2.setNombre("Ignacio");
        session.persist(user2);

        Usuario user3 = new Usuario();
        user3.setNombre("Lucía");
        session.persist(user3);

        Usuario user4 = new Usuario();
        user4.setNombre("Nuria");
        session.persist(user4);

        session.getTransaction().commit();
    }
}