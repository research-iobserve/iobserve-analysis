package org.iobserve.service.behavior.analysis.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.iobserve.service.behavior.analysis.GraphEditDistance;
import org.iobserve.service.behavior.analysis.MTreeGenerator;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import mtree.MTree;
import teetime.framework.test.StageTester;

public class MTreeGeneratorTest {

    private MTreeGenerator<BehaviorModelGED> mtreeGenerator;
    private GraphEditDistance mockGED;

    private BehaviorModelGED model1;
    private BehaviorModelGED model2;
    private BehaviorModelGED model3;
    private BehaviorModelGED model4;

    @Before
    public void setup() {
        this.mockGED = Mockito.mock(GraphEditDistance.class);

        this.mtreeGenerator = new MTreeGenerator<>(this.mockGED);

        this.model1 = Mockito.mock(BehaviorModelGED.class);
        this.model2 = Mockito.mock(BehaviorModelGED.class);
        this.model3 = Mockito.mock(BehaviorModelGED.class);
        this.model4 = Mockito.mock(BehaviorModelGED.class);

        Mockito.when(this.mockGED.calculate(this.model1, this.model1)).thenReturn(0.0);
        Mockito.when(this.mockGED.calculate(this.model2, this.model2)).thenReturn(0.0);
        Mockito.when(this.mockGED.calculate(this.model3, this.model3)).thenReturn(0.0);
        Mockito.when(this.mockGED.calculate(this.model4, this.model4)).thenReturn(0.0);

        Mockito.when(this.mockGED.calculate(this.model1, this.model2)).thenReturn(1.0);
        Mockito.when(this.mockGED.calculate(this.model1, this.model3)).thenReturn(11.0);
        Mockito.when(this.mockGED.calculate(this.model1, this.model4)).thenReturn(12.0);

        Mockito.when(this.mockGED.calculate(this.model2, this.model1)).thenReturn(1.0);
        Mockito.when(this.mockGED.calculate(this.model2, this.model3)).thenReturn(10.0);
        Mockito.when(this.mockGED.calculate(this.model2, this.model4)).thenReturn(11.0);

        Mockito.when(this.mockGED.calculate(this.model3, this.model1)).thenReturn(11.0);
        Mockito.when(this.mockGED.calculate(this.model3, this.model2)).thenReturn(10.0);
        Mockito.when(this.mockGED.calculate(this.model3, this.model4)).thenReturn(1.0);

        Mockito.when(this.mockGED.calculate(this.model4, this.model1)).thenReturn(12.0);
        Mockito.when(this.mockGED.calculate(this.model4, this.model2)).thenReturn(11.0);
        Mockito.when(this.mockGED.calculate(this.model4, this.model3)).thenReturn(1.0);

    }

    @Test
    public void emptyTest() {
        final List<BehaviorModelGED> list = new ArrayList<>();
        final List<List<BehaviorModelGED>> input = new ArrayList<>();
        input.add(list);

        final List<MTree<BehaviorModelGED>> solutions = this.startAndGetSolutions(input);

        final Iterator<MTree<BehaviorModelGED>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

        MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
        MatcherAssert.assertThat(solutions.size(), Matchers.is(1));
    }

    @Test
    public void oneElementTest() {
        final List<BehaviorModelGED> list = new ArrayList<>();
        list.add(this.model1);
        final List<List<BehaviorModelGED>> input = new ArrayList<>();
        input.add(list);
        final List<MTree<BehaviorModelGED>> solutions = this.startAndGetSolutions(input);

        final Iterator<MTree<BehaviorModelGED>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));
        MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
        MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

    }

    @Test
    public void fourElementTest() {
        final List<BehaviorModelGED> list = new ArrayList<>();
        list.add(this.model1);
        list.add(this.model2);
        list.add(this.model3);
        list.add(this.model4);

        final List<List<BehaviorModelGED>> input = new ArrayList<>();
        input.add(list);
        final List<MTree<BehaviorModelGED>> solutions = this.startAndGetSolutions(input);

        Iterator<MTree<BehaviorModelGED>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

        // System.out.println(this.mockGED.calculate(iter.next().data, this.model1));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model2));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model3));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model4));

        MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
        MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

        iter = solutions.get(0).getNearest(this.model3).iterator();

        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model3));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model4));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model2));
        MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));

    }

    private List<MTree<BehaviorModelGED>> startAndGetSolutions(final List<List<BehaviorModelGED>> input) {
        final List<MTree<BehaviorModelGED>> solutions = new ArrayList<>();
        StageTester.test(this.mtreeGenerator).and().send(input).to(this.mtreeGenerator.getInputPort()).and()
                .receive(solutions).from(this.mtreeGenerator.getOutputPort()).start();
        return solutions;
    }
}
