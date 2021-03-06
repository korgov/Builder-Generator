package pl.mjedynak.idea.plugins.builder.factory;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.mjedynak.idea.plugins.builder.factory.PsiFieldsForBuilderFactory;
import pl.mjedynak.idea.plugins.builder.psi.model.PsiFieldsForBuilder;
import pl.mjedynak.idea.plugins.builder.verifier.PsiFieldVerifier;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PsiFieldsForBuilderFactoryTest {

    @InjectMocks private PsiFieldsForBuilderFactory factory;
    @Mock private PsiFieldVerifier psiFieldVerifier;
    @Mock private PsiClass psiClass;
    @Mock private PsiElementClassMember psiElementClassMember;

    private List<PsiElementClassMember> psiElementClassMembers;

    @Mock private PsiField psiField;

    @Before
    public void setUp() {
        psiElementClassMembers = Arrays.asList(psiElementClassMember);
        given(psiElementClassMember.getPsiElement()).willReturn(psiField);
    }

    @Test
    public void shouldCreateObjectWithPsiFieldsForSetters() {
        // given
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(true);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThatFieldsForConstructorAreEmpty(result);

        List<PsiField> fieldsForSetters = result.getFieldsForSetters();
        assertThat(fieldsForSetters, is(notNullValue()));
        assertThat(fieldsForSetters.size(), is(1));
        assertThat(fieldsForSetters.get(0), is(psiField));
    }

    @Test
    public void shouldCreateObjectWithPsiFieldsForConstructor() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(true);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThatFieldsForSettersAreEmpty(result);

        List<PsiField> fieldsForConstructor = result.getFieldsForConstructor();
        assertThat(fieldsForConstructor, is(notNullValue()));
        assertThat(fieldsForConstructor.size(), is(1));
        assertThat(fieldsForConstructor.get(0), is(psiField));
    }

    @Test
    public void shouldCreateObjectWithEmptyList() {
        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThatFieldsForSettersAreEmpty(result);
        assertThatFieldsForConstructorAreEmpty(result);
    }

    @Test
    public void shouldPreferFieldsForSetterOverFieldsForConstructor() {
        // given
        given(psiFieldVerifier.isSetInConstructor(psiField, psiClass)).willReturn(true);
        given(psiFieldVerifier.isSetInSetterMethod(psiField, psiClass)).willReturn(true);

        // when
        PsiFieldsForBuilder result = factory.createPsiFieldsForBuilder(psiElementClassMembers, psiClass);

        // then
        assertThat(result, is(notNullValue()));
        assertThatFieldsForConstructorAreEmpty(result);

        List<PsiField> fieldsForSetters = result.getFieldsForSetters();
        assertThat(fieldsForSetters, is(notNullValue()));
        assertThat(fieldsForSetters.size(), is(1));
        assertThat(fieldsForSetters.get(0), is(psiField));

    }

    private void assertThatFieldsForConstructorAreEmpty(PsiFieldsForBuilder result) {
        List<PsiField> fieldsForConstructor = result.getFieldsForConstructor();
        assertThat(fieldsForConstructor, is(notNullValue()));
        assertThat(fieldsForConstructor.size(), is(0));
    }

    private void assertThatFieldsForSettersAreEmpty(PsiFieldsForBuilder result) {
        List<PsiField> fieldsForSetters = result.getFieldsForSetters();
        assertThat(fieldsForSetters, is(notNullValue()));
        assertThat(fieldsForSetters.size(), is(0));
    }

}
