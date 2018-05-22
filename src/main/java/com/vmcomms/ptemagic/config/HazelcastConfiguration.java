package com.vmcomms.ptemagic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class HazelcastConfiguration {

	@Bean
    public Config hazelCastConfig(){
		Config hazelCastConfig = new Config()
                .setInstanceName("hazelcast-instance");
                
        // Map instruments
        MapConfig examMapConfig = new MapConfig()
                .setName("exam")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxIdleSeconds(300)
                .setTimeToLiveSeconds(600);
        
        // Add map config
        hazelCastConfig.addMapConfig(examMapConfig);
        
		return hazelCastConfig;
    }
}
