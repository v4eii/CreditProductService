package ru.neoflex.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "payment_schedule", schema = "public")
@NamedQueries({
        @NamedQuery(name = "findAllPaymentSchedule", query = "SELECT p FROM PaymentSchedule p"),
        @NamedQuery(name = "findByUserLogin", query = "SELECT p FROM PaymentSchedule p WHERE userLogin = :usrLogin")
})
public class PaymentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_schedule")
    @Getter
    @Setter
    private Integer idSchedule;
    @Basic(optional = false)
    @Column(name = "user_login")
    @Getter
    @Setter
    private String userLogin;
    @Basic(optional = false)
    @Column(name = "date_generate")
    @Temporal(TemporalType.DATE)
    @Getter
    @Setter
    private Date dateGenerate;
    @Basic(optional = false)
    @Column(name = "client_name")
    @Getter
    @Setter
    private String clientName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSchedule", fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Collection<Payments> paymentsCollection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentSchedule that = (PaymentSchedule) o;

        if (idSchedule != that.idSchedule) return false;
        if (userLogin != null ? !userLogin.equals(that.userLogin) : that.userLogin != null) return false;
        if (dateGenerate != null ? !dateGenerate.equals(that.dateGenerate) : that.dateGenerate != null) return false;
        if (clientName != null ? !clientName.equals(that.clientName) : that.clientName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idSchedule;
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (dateGenerate != null ? dateGenerate.hashCode() : 0);
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        return result;
    }
}
