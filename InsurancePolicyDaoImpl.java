package in.abc.dao;

import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.Query;

import in.abc.model.InsurancePolicy;
import in.abc.util.HibernateUtil;

/*
 * DELIMITER $$
CREATE
    PROCEDURE `abc`.`GET_POLICIES_BY_TENURE`(IN tenure1 INT,IN tenure2 INT)
	BEGIN
		SELECT policyId,company,policyName,policyType,tenure FROM insurancepolicy
		 WHERE tenure>=tenure1 AND tenure<=tenure2;
	END$$
DELIMITER ;
*
*/
public class InsurancePolicyDaoImpl implements InsurancePolicyDao {

	@Override
	public List<InsurancePolicy> getPoliciesByTenure(int start, int end) {
		
		Session session = HibernateUtil.getSession();
		
		ProcedureCall procedureCall = session.createStoredProcedureCall("GET_POLICIES_BY_TENURE",
				InsurancePolicy.class);
		
		procedureCall.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(start);
		procedureCall.registerParameter(2, Integer.class, ParameterMode.IN).bindValue(end);
		
		@SuppressWarnings("unchecked")
		List<InsurancePolicy> list = procedureCall.getResultList();

		
		return list;
	}
	
	/*
	 * DELIMITER $$
	 * 
	 * CREATE
	 * 
	 * PROCEDURE `abc`.`GET_POLICY_BY_ID`(IN id INT,OUT pName VARCHAR(20),OUT cname
	 * VARCHAR(20), OUT ptype VARCHAR(20))
	 * 
	 * BEGIN SELECT policyName,company,policyType INTO pName,cname,ptype FROM
	 * INSURANCEPOLICY WHERE policyId=id; END$$
	 * 
	 * DELIMITER ;
	 * 
	 * CALL GET_POLICY_BY_ID(1,@pname,@pcompany,@ptype)

	 * 	SELECT @pname,@pcompany,@ptype
	 * 
	 */
		
	@Override
	public String[] getPolicyById(int id) {
		
		Session session = HibernateUtil.getSession();
		
		ProcedureCall procedureCall = session.createStoredProcedureCall("GET_POLICY_BY_ID");
		
		procedureCall.registerParameter(1, Integer.class, ParameterMode.IN).bindValue(id);
		procedureCall.registerParameter(2, String.class, ParameterMode.OUT);
		procedureCall.registerParameter(3, String.class, ParameterMode.OUT);
		procedureCall.registerParameter(4, String.class, ParameterMode.OUT);

		String policyName = (String) procedureCall.getOutputParameterValue(2);
		String companyName = (String) procedureCall.getOutputParameterValue(3);
		String policyType = (String) procedureCall.getOutputParameterValue(4);
		
		String result[] = {policyName,companyName,policyType};
		return result;
	}
	
}
