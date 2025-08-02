package Profile;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import Profile.Profile;
import Profile.BooleanQuestion;
import Profile.Answer;
import Profile.Criteria;
import Profile.Criterion;
import Profile.Bool;
import Profile.Weight;

public class ProfileTest {
	
    private Profile profile;
    private BooleanQuestion question;
    private Criteria criteria;
    
    @Before
    public void setUp() {
        profile = new Profile("Bull Hockey, Inc.");
        question = new BooleanQuestion(1, "Got bonuses?");
        criteria = new Criteria();
    }
    
    @Test
    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
        profile.add(new Answer(question, Bool.False.getValue()));
        criteria.add(new Criterion(new Answer(question, Bool.True.getValue()), Weight.MustMatch));
        
        boolean matches = profile.matches(criteria);
        assertFalse("Expected match to be false when MustMatch criteria are not met", matches);
    }
    
    @Test
    public void matchAnswersTrueForAnyDontCareCriteria() {
        profile.add(new Answer(question, Bool.False.getValue()));
        criteria.add(new Criterion(new Answer(question, Bool.True.getValue()), Weight.DontCare));
        
        boolean matches = profile.matches(criteria);
        assertTrue("Expected match to be true when using DontCare criteria", matches);
    }

//    private Profile profile;
//    private BooleanQuestion question;
//    private Criteria criteria;
//
//    @Before
//    public void create() {
//        profile = new Profile("Bull Hockey, Inc.");
//        question = new BooleanQuestion(1, "Got bonuses?");
//        criteria = new Criteria();
//    }
//
//    @Test
//    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
//        profile.add(new Answer(question, Bool.False.getValue()));
//        criteria.add(new Criterion(new Answer(question, Bool.True.getValue()), Weight.MustMatch));
//        
//        boolean matches = profile.matches(criteria);
//        assertFalse("Expected match to be false when MustMatch criteria are not met", matches);
//    }
//
//    @Test
//    public void matchAnswersTrueForAnyDontCareCriteria() {
//        profile.add(new Answer(question, Bool.False.getValue()));
//        criteria.add(new Criterion(new Answer(question, Bool.True.getValue()), Weight.DontCare));
//        
//        boolean matches = profile.matches(criteria);
//        assertTrue("Expected match to be true for DontCare criteria", matches);
//    }
	
//    @Test
//    public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
//        Profile profile = new Profile("Bull Hockey, Inc.");
//        BooleanQuestion question = new BooleanQuestion(1, "Got bonuses?");
//        Answer profileAnswer = new Answer(question, Bool.False.getValue());
//        profile.add(profileAnswer);
//        Criteria criteria = new Criteria();
//        Answer criteriaAnswer = new Answer(question, Bool.True.getValue());
//        Criterion criterion = new Criterion(criteriaAnswer, Weight.MustMatch);
//        criteria.add(criterion);
//        boolean matches = profile.matches(criteria);
//        assertFalse("Expected match to be false when MustMatch criteria are not met", matches);
//    }
//    
//    @Test
//    public void matchAnswersTrueForAnyDontCareCriteria() {
//        Profile profile = new Profile("Bull Hockey, Inc.");
//        BooleanQuestion question = new BooleanQuestion(1, "Got milk?");
//        Answer profileAnswer = new Answer(question, Bool.False.getValue());
//        profile.add(profileAnswer);
//        Criteria criteria = new Criteria();
//        Answer criteriaAnswer = new Answer(question, Bool.True.getValue());
//        Criterion criterion = new Criterion(criteriaAnswer, Weight.DontCare);
//        criteria.add(criterion);
//        boolean matches = profile.matches(criteria);
//        assertTrue("Expected match to be true when using DontCare criteria", matches);
//    }

}