using AutoMapper;
using ServiceLogicLayer.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using ServiceLogicLayer.Models.AccountModels;

namespace UniversityStudentRatingService.App_Start
{
    public static class AutoMapperConfig
    {
        public static void Configure()
        {
            ServiceLogicLayer.Configuration.AutoMapperConfig.Configure();
            Mapper.CreateMap<UserDTO, RegisterModel>().ReverseMap();
            Mapper.CreateMap<UserDTO, LoginModel>().ReverseMap();
        }
    }
}