package com.javarush.task.task37.task3702;

import com.javarush.task.task37.task3702.female.FemaleFactory;
import com.javarush.task.task37.task3702.male.MaleFactory;

public class FactoryProducer {
    public static enum HumanFactoryType {MALE, FEMALE};
    public static <T extends AbstractFactory> T getFactory(HumanFactoryType humanFactoryType){
        if (humanFactoryType == HumanFactoryType.MALE) {
            return (T) new MaleFactory();
        } else return (T) new FemaleFactory();
    }
}
