using ServiceLogicLayer.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DataAccessLayer.UnitOfWork;
using DataAccessLayer.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin;
using Microsoft.Owin.Security;

namespace ServiceLogicLayer.Services
{
    public class ServiceCreator : IServiceCreator
    {
        IOwinContext context;
        IAuthService authService;
        IUserService userService;
        IDataService dataService;
        IStatisticService statisticService;
        ISearchService searchService;

        public IAuthService AuthService
        {
            get
            {
                return authService ?? new AuthService(new UnitOfWork(context)); 
            }
        }

        public IUserService UserService
        {
            get
            {
                return userService ?? new UserService(new UnitOfWork(context));
            }
        }

        public IDataService DataService
        {
            get
            {
                return dataService ?? new DataService(new UnitOfWork(context));
            }
        }

        public IStatisticService StatisticService
        {
            get
            {
                return statisticService ?? new StatisticService(new UnitOfWork(context));
            }
        }

        public ISearchService SearchService
        {
            get
            {
                return searchService ?? new SearchService(new UnitOfWork(context));
            }
        }


        public ServiceCreator(IOwinContext context)
        {
            this.context = context;
        }

        public void Dispose()
        {
            if (authService != null) authService.Dispose();
            if (userService != null) userService.Dispose();
            if (dataService != null) dataService.Dispose();
        }
    }
}
