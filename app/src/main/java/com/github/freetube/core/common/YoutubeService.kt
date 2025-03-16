package com.github.freetube.core.common

import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.YoutubeService

val youtubeService = NewPipe.getService(ServiceList.YouTube.serviceId)
