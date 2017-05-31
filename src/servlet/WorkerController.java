package servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hibernate.HibernateUtil;
import model.Worker;

@Controller
public class WorkerController {
	
	/*---------��¼����---------*/
	@RequestMapping("login.do")
	public String Login(@RequestParam(value="id")String id, @RequestParam(value="pwd")String pwd, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			if(id==null ||id.equals(""))
				throw new Exception("�������û���");
			if(pwd==null)
				pwd="";
			session.beginTransaction();
			Worker worker=(Worker) session.get(Worker.class, id);
			session.getTransaction().commit();
			if(worker==null) 
				throw new Exception("�û�������");
			if(!pwd.equals(worker.getPwd()))
				throw new Exception("�������");
			request.getSession().setAttribute("ID",worker.getId());
			if(worker.getType().equals("admin"))
				return "admin";
			else if(worker.getType().equals("mod"))
				return "redirect:/selectnextpicture.do";
			else if(worker.getType().equals("check"))
				return "redirect:/selectnextpicturecheck.do";
			else
				return "login";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------�޸�ְ��----------*/
	@RequestMapping("modtype.do")
	public String ModType(@RequestParam(value="id")String id, @RequestParam(value="type")String type, HttpServletRequest request) {
		try {
			if(type.equals("0"))
				throw new Exception("��ѡ��ְ��");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Worker worker = (Worker) session.get(Worker.class, id);
			if(worker == null) 
				throw new Exception("�û�������");
			worker.setType(type);
			session.update(worker);
			session.getTransaction().commit();
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------��ѯȫ��----------*/
	@RequestMapping("selectworker.do")
	public String SelectWorker(HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker";  
	        Query query = session.createQuery(hql);  
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			request.setAttribute("cname", "worker");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------ְλ��ѯ----------*/
	@RequestMapping("selecttype.do")
	public String SelectType(@RequestParam(value="type")String type, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker where type=?";
	        Query query = session.createQuery(hql);
	        query.setString(0, type);
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------����id��ѯ----------*/
	@RequestMapping("selectworkerid.do")
	public String SelectWorkerId(@RequestParam(value="id")String id, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker where id=?";  
	        Query query = session.createQuery(hql);  
	        query.setString(0, id);
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			request.setAttribute("cname", "worker");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*---------�ǳ�---------*/
	@RequestMapping("out.do")
	public String Out(HttpServletRequest request) {
		request.getSession().invalidate();
        return "login";
	}
	
}
