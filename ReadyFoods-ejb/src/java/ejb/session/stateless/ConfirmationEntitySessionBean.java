/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConfirmationEntity;
import entity.OrderEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PYT
 */
@Stateless
public class ConfirmationEntitySessionBean implements ConfirmationEntitySessionBeanLocal {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @PersistenceContext(unitName = "ReadyFoods-ejbPU")
    private EntityManager em;
    
    
    public Long createNewConfirmationEntity(Long orderId,ConfirmationEntity confirmationEntity){
        OrderEntity orderEntity = em.find(OrderEntity.class,orderId);
        confirmationEntity.setOrderEntity(orderEntity);
        em.persist(confirmationEntity);
        em.flush();
        return confirmationEntity.getId();
    }
   
}
