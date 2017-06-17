using Microsoft.Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Interfaces
{
    public interface IServiceCreator : IDisposable
    {
        IAuthService AuthService { get; }
        IUserService UserService { get; }
        IDataService DataService { get; }
        IStatisticService StatisticService { get; }
        ISearchService SearchService { get; }
    }
}
