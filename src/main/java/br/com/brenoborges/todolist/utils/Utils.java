package br.com.brenoborges.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    /**
     * Metodo para copiar propriedades não nulas.
     * 
     * @param source
     * @param target
     */
    public static void copyNonNullProperties(Object source, Object target) {
        // Classe do Java que copia as propriedadades. Ultimo parametro pode ser uma
        // classe, passo o meu método que retorna as propriedades nulas.
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * Metodo para buscar as propriedades com nome nulo.
     * 
     * @param source
     * @return array de String com as propriedades nulas
     */
    public static String[] getNullPropertyNames(Object source) {
        // BeanWrapper é uma interface que fornece formas de acessar propriedades de um
        // objeto.
        // BeanWrapperImpl é a implementação dessa interface.
        final BeanWrapper src = new BeanWrapperImpl(source);

        // Gera um array com todas as propriedades descritas no objeto.
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        // Conjunto com as propriedades com valores nulos.
        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : pds) {
            // Pega o nome da propriedade.
            Object srcValue = src.getPropertyValue(pd.getName());
            // Verifica se o nome da propriedade é nulo.
            if (srcValue == null) {
                // Se o nome for nulo, adiciona o nome no meu array emptyNames.
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];

        // Convertendo o conjunto de propriedades para um array de String.
        return emptyNames.toArray(result);
    }
}
