using AutoMapper;
using DataAccessLayer.DbContext;
using DataAccessLayer.Entities;
using ServiceLogicLayer.DTO;
using ServiceLogicLayer.Models.AccountModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Configuration
{
    public static class AutoMapperConfig
    {
        public static void Configure()
        {
            Mapper.CreateMap<UserDTO, UserProfile>().ReverseMap();
            Mapper.CreateMap<LoginModel, UserProfile>().ReverseMap();
            Mapper.CreateMap<RegisterModel, UserProfile>().ReverseMap();
            Mapper.CreateMap<UserDTO, ApplicationUser>().ReverseMap();
            Mapper.CreateMap<AuthUserDTO, ApplicationUser>().ReverseMap();
            Mapper.CreateMap<LoginModel, ApplicationUser>().ReverseMap();
            Mapper.CreateMap<RegisterModel, ApplicationUser>().ReverseMap();
        }
    }
}
