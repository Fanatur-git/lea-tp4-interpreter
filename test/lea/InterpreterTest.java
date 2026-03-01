package lea;

import org.junit.jupiter.api.Test;
import lea.Node.*;
import lea.Reporter.Phase;

public final class InterpreterTest {

	/* =========================
	 * === CALCULS ARITHMÉTIQUES
	 * ========================= */

	@Test
	void arithmetic_operations() {
		new LeaAsserts("écrire(10 + 5 * 2 - 8);")
		.assertOutputs(new Int(12));
	}

	@Test
	void unary_minus() {
		new LeaAsserts("""
				x <- 5;
				écrire(-x);
				écrire(-(-10));
				""")
		.assertOutputs(new Int(-5), new Int(10));
	}

	/* =========================
	 * === LOGIQUE ET COMPARAISONS
	 * ========================= */

	@Test
	void boolean_logic() {
		new LeaAsserts("""
				écrire(vrai et (faux ou vrai));
				écrire(1 < 2);
				écrire(5 = 5);
				""")
		.assertOutputs(new Bool(true), new Bool(true), new Bool(true));
	}

	@Test
	void equality_different_types() {
		new LeaAsserts("écrire(5 = vrai);")
		.assertOutputs(new Bool(false));
	}

	/* =========================
	 * === VARIABLES ET PORTÉE
	 * ========================= */

	@Test
	void variable_assignment_and_usage() {
		new LeaAsserts("""
				a <- 10;
				b <- a + 5;
				a <- 20;
				écrire(a);
				écrire(b);
				""")
		.assertOutputs(new Int(20), new Int(15));
	}

	/* =========================
	 * === STRUCTURES DE CONTRÔLE (SI)
	 * ========================= */

	@Test
	void if_else_branching() {
		new LeaAsserts("""
				x <- 10;
				si x < 5 alors
				    écrire(1);
				sinon
				    écrire(2);
				fin si
				""")
		.assertOutputs(new Int(2));
	}

	/* =========================
	 * === ERREURS À L'EXÉCUTION (Phase.RUNTIME)
	 * ========================= */

	@Test
	void error_undefined_variable() {
		new LeaAsserts("écrire(z);")
		.assertHasErrorContaining(Phase.RUNTIME, "variable pas initialisée");
	}

	@Test
	void error_type_mismatch_arithmetic() {
		new LeaAsserts("x <- 1 + vrai;")
		.assertHasErrorContaining(Phase.RUNTIME, "Type (entier)");
	}

	@Test
	void error_type_mismatch_condition() {
		new LeaAsserts("""
				si 1 alors
					écrire(1);
				sinon
					écrire(2);
				fin si
				""")
		.assertHasErrorContaining(Phase.RUNTIME, "Type (booléen)");
	}

	@Test
	void error_logical_op_on_ints() {
		new LeaAsserts("écrire(1 et 2);")
		.assertHasErrorContaining(Phase.RUNTIME, "Type (booléen)");
	}
}