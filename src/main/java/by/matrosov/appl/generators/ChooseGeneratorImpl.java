package by.matrosov.appl.generators;

public class ChooseGeneratorImpl implements ChooseGenerator {
    @Override
    public boolean choose(int i) {
        return i == 0;
    }
}
