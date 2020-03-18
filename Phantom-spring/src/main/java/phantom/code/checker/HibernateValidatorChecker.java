package phantom.code.checker;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import phantom.code.rule.AnyExistRule;
import phantom.code.rule.CheckRule;
import phantom.code.rule.GenericConstraintRule;
import phantom.code.rule.MappingRule;
import phantom.code.rule.Rule;
import phantom.code.rule.ValidRule;

/**
 * hibernate-validator检查器
 * @author Frodez
 */
@Component
public class HibernateValidatorChecker extends CodeChecker {

	private static final List<Rule> rules = new ArrayList<>();

	static {
		rules.add(new ValidRule());
		rules.add(new MappingRule());
		rules.add(new GenericConstraintRule());
		rules.add(new CheckRule());
		rules.add(new AnyExistRule());
	}

	public HibernateValidatorChecker() {
		super(rules);
	}

}
