package dev.glimpse.messenger.user.infrastructure;

import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.inject.GrpcClientBeans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import profiles.ProfileServiceGrpc;

@GrpcClientBeans(value = {
        @GrpcClientBean(
                clazz = ProfileServiceGrpc.ProfileServiceBlockingStub.class,
                beanName = "profileServiceBlockingStub",
                client = @GrpcClient("profile-service")
        )
})
@Configuration
public class GrpcProfileConfiguration {

    @Bean
    public GrpcFindingUserProfilesUseCase grpcProfileService(@Autowired ConversionService conversionService,
                                                             @Autowired ProfileServiceGrpc.ProfileServiceBlockingStub profileServiceBlockingStub) {
        return new GrpcFindingUserProfilesUseCase(conversionService, profileServiceBlockingStub);
    }

}
