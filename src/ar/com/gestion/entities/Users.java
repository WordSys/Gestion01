/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUsersId", query = "SELECT u FROM Users u WHERE u.usersId = :usersId")
    , @NamedQuery(name = "Users.findByUsersUser", query = "SELECT u FROM Users u WHERE u.usersUser = :usersUser")
    , @NamedQuery(name = "Users.findByUsersPass", query = "SELECT u FROM Users u WHERE u.usersPass = :usersPass")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "users_id")
    private Integer usersId;
    @Basic(optional = false)
    @Column(name = "users_user")
    private String usersUser;
    @Basic(optional = false)
    @Column(name = "users_pass")
    private String usersPass;
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    @ManyToOne(optional = false)
    private Empleado empId;
    private Integer emp_Id;

    public Users() {
    }

    public Users(Integer usersId) {
        this.usersId = usersId;
    }

    public Users(Integer usersId, String usersUser, String usersPass) {
        this.usersId = usersId;
        this.usersUser = usersUser;
        this.usersPass = usersPass;
    }
    
    public Users(Integer usersId,int emp_Id, String usersUser, String usersPass) {
        this.usersId = usersId;
        this.emp_Id = emp_Id;
        this.usersUser = usersUser;
        this.usersPass = usersPass;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
    }

    public String getUsersUser() {
        return usersUser;
    }

    public void setUsersUser(String usersUser) {
        this.usersUser = usersUser;
    }

    public String getUsersPass() {
        return usersPass;
    }

    public void setUsersPass(String usersPass) {
        this.usersPass = usersPass;
    }

    public Empleado getEmpId() {
        return empId;
    }

    public void setEmpId(Empleado empId) {
        this.empId = empId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usersId != null ? usersId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.usersId == null && other.usersId != null) || (this.usersId != null && !this.usersId.equals(other.usersId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Users[ usersId=" + usersId + " ]";
    }
    
}
