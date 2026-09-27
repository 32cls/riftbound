package org.acme;

import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.PatternBasedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.FunctionParameterType;
import org.hibernate.query.sqm.produce.function.StandardArgumentsValidators;
import org.hibernate.query.sqm.produce.function.StandardFunctionArgumentTypeResolvers;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.produce.function.internal.PatternRenderer;
import org.hibernate.type.StandardBasicTypes;

public class PostgisDistanceFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        final PatternRenderer renderer = new PatternRenderer(
            "ST_Distance(ST_SetSRID(?1, 4326)::geography, ST_SetSRID(?2, 4326)::geography)"
        );
        functionContributions.getFunctionRegistry().register(
            "distance_meters",
            new PatternBasedSqmFunctionDescriptor(
                renderer,
                StandardArgumentsValidators.exactly(2),
                StandardFunctionReturnTypeResolvers.invariant(
                    functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE)
                ),
                StandardFunctionArgumentTypeResolvers.invariant(FunctionParameterType.SPATIAL, FunctionParameterType.SPATIAL),
                "distance_meters",
                FunctionKind.NORMAL,
                "(SPATIAL, SPATIAL)"
            )
        );
    }

    @Override
    public int ordinal() {
        return 100;
    }
}
